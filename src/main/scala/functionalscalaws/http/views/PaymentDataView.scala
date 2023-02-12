// package functionalscalaws.http.views

// import java.util.UUID

// import functionalscalaws.domain.db.PaymentData
// import zio.json.DeriveJsonEncoder
// import zio.json.JsonEncoder

// final case class PaymentDataView(
//     id: UUID,
//     name: String,
//     amount: BigDecimal,
//     recipient: String
// )
// object PaymentDataView {
//   implicit val encoder: JsonEncoder[PaymentDataView] = DeriveJsonEncoder.gen[PaymentDataView]

//   def fromPaymentData(payment: PaymentData): PaymentDataView =
//     PaymentDataView(payment.id, payment.name.value, payment.amount.value, payment.recipient.value)
// }
