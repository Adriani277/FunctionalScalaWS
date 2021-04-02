package functionalscalaws.http

import functionalscalaws.http.views._
import functionalscalaws.program._
import functionalscalaws.services.db.RepositoryAlg
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import zio._
import zio.interop.catz._
import zio.json._
import zio.json.ast.Json

final class PaymentsEndpoint[R <: Has[PaymentCreation] with Has[PaymentUpdateProgram] with Has[
  RepositoryAlg.PaymentRepository
]] {
  type PaymentsTask[A] = RIO[R, A]

  private val dsl          = Http4sDsl[PaymentsTask]
  implicit val jsonEncoder = ZioJsonEntityCodec.jsonEncoderOf[Json]
  import dsl._
  val routes =
    HttpRoutes.of[PaymentsTask] {
      case GET -> Root / "status" =>
        Ok("""{"status" : "ok"}""")

      case r @ POST -> Root / "payment" / "create" =>
        implicit val ed = ZioJsonEntityCodec.jsonOf[PaymentsTask, PaymentView]
        for {
          view   <- r.as[PaymentView]
          result <- PaymentCreation.create(PaymentView.toPayment(view)).either
          response <- result match {
            case Left(e)  => UnprocessableEntity(ErrorResponse.fromServiceError(e).toJson)
            case Right(v) => Created(PaymentDataView.fromPaymentData(v).toJson)
          }
        } yield response

      case r @ PUT -> Root / "payment" / "update" =>
        implicit val ed = ZioJsonEntityCodec.jsonOf[PaymentsTask, AmountUpdateView]
        for {
          view   <- r.as[AmountUpdateView]
          result <- PaymentUpdateProgram.update(AmountUpdateView.toAmountUpdate(view)).either
          response <- result match {
            case Left(e)  => UnprocessableEntity(ErrorResponse.fromServiceError(e).toJson)
            case Right(v) => Ok(PaymentDataView.fromPaymentData(v).toJson)
          }
        } yield response

      case GET -> Root / "payments" =>
        for {
          payments <- RepositoryAlg.selectAll
          result   <- Ok(payments.map(PaymentDataView.fromPaymentData).toJson)
        } yield result
    }
}
