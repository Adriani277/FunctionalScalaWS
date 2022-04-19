package functionalscalaws.http.views

import functionalscalaws.domain._
import zio.json._

final case class PaymentView private (name: String, amount: Double, recipient: String)
object PaymentView {
  implicit val coded: JsonCodec[PaymentView] = zio.json.DeriveJsonCodec.gen[PaymentView]

  def toPayment(view: PaymentView): Payment =
    Payment(
      Name(view.name),
      Amount(view.amount),
      Recipient(view.recipient)
    )
}
