package functionalscalaws.http.views

import zio.json._

final case class NoteTitlesView(titles: List[String])
object NoteTitlesView:
  given JsonEncoder[NoteTitlesView] = DeriveJsonEncoder.gen[NoteTitlesView]
  given JsonDecoder[NoteTitlesView] = DeriveJsonDecoder.gen[NoteTitlesView]
