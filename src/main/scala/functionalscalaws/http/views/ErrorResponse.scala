// package functionalscalaws.http.views

// import functionalscalaws.domain._
// import zio.json.DeriveJsonCodec

// final case class ErrorResponse(error_message: String, error: String)
// object ErrorResponse {
//   implicit val codec = DeriveJsonCodec.gen[ErrorResponse]

//   val fromServiceError: ServiceError => ErrorResponse = {
//     case InvalidAmountError(amount) =>
//       ErrorResponse(s"Provided amount [${amount.value}] has to be greater than 0", "invalid_amount")
//     case InvalidTransactionError =>
//       ErrorResponse("Payer name cannot be the same as recepient name", "invalid_transaction")
//   }
// }
