package functionalscalaws.http

// import functionalscalaws.program._
import functionalscalaws.services.db.RepositoryAlg
import zio.json._
import zio.http._
import zio.http.model.Method
import zio._
import functionalscalaws.http.views._
import zio.http.model.Status
import zio.http.model.Headers.Header
import zio.http.model.Method._
import functionalscalaws.domain.db.PaymentData
final case class PaymentsEndpoint(private val db: RepositoryAlg[PaymentData]) {
  val routes = Http.collectZIO {
    case r @ POST -> !! / "payment" / "create" =>
      for {
        viewString <- r.body.asString
        _          <- ZIO.logInfo(s"Received request: $viewString")
        view       <- ZIO.fromEither(viewString.fromJson[PaymentView])
        payment    <- PaymentView.toPayment(view)
        result     <- db.create(payment)
        _          <- ZIO.logInfo(s"Created payment with id: ${result.id}")
      } yield {
        Response.json(PaymentDataView.fromPaymentData(result).toJson).setStatus(Status.Created)
      }

    case GET -> !! / "payments" =>
      for {
        payments <- db.selectAll
        result = Response.json(payments.map(PaymentDataView.fromPaymentData).toJson)
      } yield result
  }
}

object PaymentsEndpoint {
  val live = ZLayer.fromZIO(ZIO.service[RepositoryAlg[PaymentData]].map(new PaymentsEndpoint(_)))
}
//   private def jsonResponse[A] =
//     (status: Status, data: String) =>
//       Response.http(
//         status,
//         List(Header.contentTypeJson),
//         HttpContent.Complete(
//           data
//         )
//       )
//  val routes = Ht
//   val routes = Http.collectM {
//     case r @ POST -> Root / "payment" / "create" =>
//       for {
//         viewString <- ZIO
//           .fromOption(r.getBodyAsString)
//           .mapError(_ => new RuntimeException("Body not found"))
//         view <- ZIO
//           .fromEither(viewString.fromJson[PaymentView])
//           .mapError(e => new RuntimeException(e))
//         result <- PaymentCreation.create(PaymentView.toPayment(view)).either
//         response = result match {
//           case Left(e) =>
//             jsonResponse(
//               Status.UNPROCESSABLE_ENTITY,
//               ErrorResponse.fromServiceError(e).toJson
//             )
//           case Right(v) =>
//             jsonResponse(
//               Status.CREATED,
//               PaymentDataView
//                 .fromPaymentData(v)
//                 .toJson
//             )
//         }
//       } yield response

//     case r @ PUT -> Root / "payment" / "update" =>
//       for {
//         viewString <- ZIO
//           .fromOption(r.getBodyAsString)
//           .mapError(_ => new RuntimeException("Body not found"))
//         view <- ZIO
//           .fromEither(viewString.fromJson[AmountUpdateView])
//           .mapError(e => new RuntimeException(e))
//         result <- PaymentUpdateProgram.update(AmountUpdateView.toAmountUpdate(view)).either
//         response = result match {
//           case Left(e) =>
//             jsonResponse(Status.UNPROCESSABLE_ENTITY, ErrorResponse.fromServiceError(e).toJson)
//           case Right(v) => Response.jsonString(PaymentDataView.fromPaymentData(v).toJson)
//         }
//       } yield response

//     case GET -> Root / "payments" =>
//       for {
//         payments <- RepositoryAlg.selectAll
//         result = Response.jsonString(payments.map(PaymentDataView.fromPaymentData).toJson)
//       } yield result

//     case GET -> Root / "status" => UIO(Response.jsonString("""{"status":"ok"}"""))
//   }
// }
