package functionalscalaws.http.views

import io.circe.Codec
import io.circe.generic.semiauto._
import functionalscalaws.domain._

final case class PaymentView private (name: String, amount: Double, recipient: String)
object PaymentView {
  implicit val coded: Codec[PaymentView] = deriveCodec[PaymentView]

  def toPayment(view: PaymentView): Payment =
    Payment(
      Name(view.name),
      Amount(view.amount),
      Recipient(view.recipient)
    )
}
