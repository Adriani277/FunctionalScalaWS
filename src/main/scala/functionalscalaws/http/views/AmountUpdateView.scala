package functionalscalaws.http.views

import java.util.UUID

import functionalscalaws.domain._
import zio.json._

final case class AmountUpdateView(id: UUID, amount: Double)
object AmountUpdateView {
  implicit val encoder: JsonEncoder[AmountUpdateView] = DeriveJsonEncoder.gen[AmountUpdateView]
  implicit val decoder: JsonDecoder[AmountUpdateView] = DeriveJsonDecoder.gen[AmountUpdateView]

  def toAmountUpdate(view: AmountUpdateView): AmountUpdate =
    AmountUpdate(view.id, Amount(BigDecimal(view.amount)))

}
