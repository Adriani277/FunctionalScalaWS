package functionalscalaws.domain

final case class Note(title: String, content: Option[String], tags: List[String])
