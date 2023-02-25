package functionalscalaws.http.views

import functionalscalaws.domain._
import zio.json._
import zio.ZIO

final case class PaymentView private (name: String, amount: Double, beneficiary: String)
object PaymentView {
  implicit val coded: JsonCodec[PaymentView] = zio.json.DeriveJsonCodec.gen[PaymentView]

  def toPayment(view: PaymentView): ZIO[Any, Nothing, Payment] =
    zio.Random.nextUUID.map { id =>
      Payment(
        id,
        Name(view.name),
        Amount(view.amount),
        Beneficiary(view.beneficiary)
      )
    }
}
