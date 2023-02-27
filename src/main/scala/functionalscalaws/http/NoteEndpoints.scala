package functionalscalaws.http

import functionalscalaws.services.NoteServiceAlg
import zio.http._
import zio.http.model.Method.{GET, POST}
import zio.ZIO
import functionalscalaws.http.views._
import zio.json._
import zio.http.model.Status
import zio.ZLayer
import zio.json.ast.Json

final case class NoteEndpoints(private val service: NoteServiceAlg):
  val routes = Http.collectZIO {
    case r @ POST -> !! / "note" / "create" =>
      val result = for {
        viewString <- r.body.asString
        _          <- ZIO.logInfo(s"Received request: $viewString")
        view       <- ZIO.fromEither(viewString.fromJson[NoteCreationView])
        note = NoteCreationView.toNote(view)
        _ <- service.create(note)
        _ <- ZIO.logInfo(s"Created note with title: ${note.title}")
      } yield {
        Response.json(NoteCreationView.fromNote(note).toJson).setStatus(Status.Created)
      }
      result.tapDefect(e => ZIO.logErrorCause("Error while creating note", e))

    case GET -> !! / "notes" =>
      for {
        notes <- service.getAll
        result = Response.json(notes.map(NoteCreationView.fromNote).toJson)
      } yield result

    case GET -> !! / "notes" / "titles" =>
      for {
        titles <- service.getAllTitles
        result = Response.json(NoteTitlesView(titles).toJson)
      } yield result
  }

object NoteEndpoints:
  val live = ZLayer.fromZIO(ZIO.service[NoteServiceAlg].map(new NoteEndpoints(_)))
