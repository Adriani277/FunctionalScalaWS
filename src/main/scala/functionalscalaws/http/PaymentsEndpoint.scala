// package functionalscalaws.http

// import functionalscalaws.http.views._
// import functionalscalaws.program._
// import functionalscalaws.services.db.RepositoryAlg
// import zhttp.http.Method._
// import zhttp.http._
// import zio._
// import zio.json._

// object PaymentsEndpoint {
//   private def jsonResponse[A] =
//     (status: Status, data: String) =>
//       Response.http(
//         status,
//         List(Header.contentTypeJson),
//         HttpContent.Complete(
//           data
//         )
//       )

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
