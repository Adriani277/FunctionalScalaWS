package functionalscalaws.db

import zio._
import NoteRepositoryAlg._
import functionalscalaws.domain._
import io.getquill.jdbczio.Quill
import io.getquill._

trait NoteRepositoryAlg:
  def create(note: Notes): UIO[Unit]
  def getAll: UIO[List[Notes]]
  def getAllTitles: UIO[List[String]]

object NoteRepositoryAlg:
  final case class Notes(title: String, content: Option[String], tags: Option[String])

final case class NoteRepository(quill: Quill.H2[SnakeCase]) extends NoteRepositoryAlg:
  import quill._

  def create(note: Notes): UIO[Unit] =
    run {
      quote {
        query[Notes]
          .insert(
            _.title   -> lift(note.title),
            _.content -> lift(note.content),
            _.tags    -> lift(note.tags)
          )
      }
    }.flatMap {
      case 1 => ZIO.unit
      case _ => ZIO.fail(new Exception("Error creating note"))
    }.orDie

  def getAll: UIO[List[Notes]] =
    run {
      query[Notes]
    }.orDie

  def getAllTitles: UIO[List[String]] =
    run {
      query[Notes].map(_.title)
    }.orDie

object NoteRepository:
  def live = ZLayer.fromZIO(ZIO.service[Quill.H2[SnakeCase]].map(NoteRepository(_)))
