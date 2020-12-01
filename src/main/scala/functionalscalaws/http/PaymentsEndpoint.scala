package functionalscalaws.http

import functionalscalaws.http.views._
import functionalscalaws.program._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import zio.RIO
import zio.interop.catz._
import zio.interop.catz._

final class PaymentsEndpoint[R <: PaymentCreationP with PaymentUpdateP] {
  type PaymentsTask[A] = RIO[R, A]

  private val dsl = Http4sDsl[PaymentsTask]
  import dsl._

  val routes =
    HttpRoutes.of[PaymentsTask] {
      case GET -> Root / "status" =>
        Ok("""{"status" : "ok"}""")

      case r @ POST -> Root / "payment" / "create" =>
        implicit val ed = jsonOf[PaymentsTask, PaymentView]
        for {
          view   <- r.as[PaymentView]
          result <- PaymentCreationP.create(PaymentView.toPayment(view)).either
          response <- result match {
            case Left(e)  => UnprocessableEntity(ErrorResponse.fromServiceError(e).asJson)
            case Right(v) => Created(PaymentDataView.fromPaymentData(v).asJson)
          }
        } yield response

      case r @ PUT -> Root / "payment" / "update" =>
        implicit val ed = jsonOf[PaymentsTask, AmountUpdateView]
        for {
          view   <- r.as[AmountUpdateView]
          result <- PaymentUpdateP.update(AmountUpdateView.toAmountUpdate(view)).either
          response <- result match {
            case Left(e)  => UnprocessableEntity(ErrorResponse.fromServiceError(e).asJson)
            case Right(v) => Ok(PaymentDataView.fromPaymentData(v).asJson)
          }
        } yield response
    }
}
