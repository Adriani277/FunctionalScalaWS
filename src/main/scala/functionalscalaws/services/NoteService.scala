package functionalscalaws.services

import functionalscalaws.db.NoteRepositoryAlg
import functionalscalaws.domain.Note
import zio._

import functionalscalaws.db.NoteRepositoryAlg
trait NoteServiceAlg:
  def create(note: Note): UIO[Unit]
  def getAll: UIO[List[Note]]
  def getAllTitles: UIO[List[String]]

final case class NoteService(private val repo: NoteRepositoryAlg) extends NoteServiceAlg:
  def create(note: Note): UIO[Unit] =
    val tagString = note.tags.foldLeft(Option.empty[String])((acc, tag) =>
      acc match
        case None        => Some(tag)
        case Some(value) => Some(value + "|" + tag)
    )
    repo.create(NoteRepositoryAlg.Notes(note.title, note.content, tagString))

  def getAll: UIO[List[Note]] =
    val tags = (tags: Option[String]) => tags.foldLeft(List.empty[String])(_ ++ _.split('|').toList)
    for {
      notes <- repo.getAll
    } yield notes.map(note => Note(note._1, note._2, tags(note._3)))

  def getAllTitles: UIO[List[String]] =
    repo.getAllTitles

object NoteService:
  val live = ZLayer.fromZIO(ZIO.service[NoteRepositoryAlg].map(NoteService(_)))
