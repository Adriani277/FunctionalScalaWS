package functionalscalaws.http

import zio.Task
import org.http4s.HttpRoutes
import zio.interop.catz._
import org.http4s.dsl.Http4sDsl
import zio.URIO
import functionalscalaws.program._
import functionalscalaws.http.views._
import zio.interop.catz._
import org.http4s.circe._
import io.circe.syntax._

object PaymentsEndpoint extends Http4sDsl[Task] {
  val service = URIO.access[PaymentCreationP with PaymentUpdateP](
    prog =>
      HttpRoutes.of[Task] {
        case GET -> Root / "status" =>
          Ok("""{"status" : "ok"}""")

        case r @ POST -> Root / "payment" / "create" =>
          implicit val ed = jsonOf[Task, PaymentView]
          val result = for {
            view   <- r.as[PaymentView]
            result <- PaymentCreationP.create(PaymentView.toPayment(view)).either
            response <- result match {
              case Left(e)  => UnprocessableEntity(ErrorResponse.fromServiceError(e).asJson)
              case Right(v) => Created(PaymentDataView.fromPaymentData(v).asJson)
            }
          } yield response

          result.provide(prog)

        case r @ PUT -> Root / "payment" / "update" =>
          implicit val ed = jsonOf[Task, AmountUpdateView]
          val result = for {
            view   <- r.as[AmountUpdateView]
            result <- PaymentUpdateP.update(AmountUpdateView.toAmountUpdate(view)).either
            response <- result match {
              case Left(e)  => UnprocessableEntity(ErrorResponse.fromServiceError(e).asJson)
              case Right(v) => Ok(PaymentDataView.fromPaymentData(v).asJson)
            }
          } yield response

          result.provide(prog)
      }
  )
}
