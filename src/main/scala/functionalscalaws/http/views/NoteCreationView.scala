package functionalscalaws.http.views

import zio.schema.codec.JsonCodec._
import zio.json._
import functionalscalaws.domain.Note

final case class NoteCreationView(title: String, content: Option[String], tags: List[String])

object NoteCreationView:
  given JsonDecoder[NoteCreationView] = DeriveJsonDecoder.gen[NoteCreationView]
  given JsonEncoder[NoteCreationView] = DeriveJsonEncoder.gen[NoteCreationView]

  val toNote: NoteCreationView => Note = view => Note(view.title, view.content, view.tags)
  val fromNote: Note => NoteCreationView = note =>
    NoteCreationView(note.title, note.content, note.tags)
