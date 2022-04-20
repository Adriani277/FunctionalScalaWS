package functionalscalaws.persistence

import zio._

trait UserPersistenceAlg[A] {
  def get(id: Int): Task[Option[A]]
  def create(entity: User): Task[A]
  def delete(id: Int): Task[Unit]
}

final case class InMemoryUserPersistence(private val mem: Vector[User])
    extends UserPersistenceAlg[User] {
  private val storageR: UIO[Ref[Vector[User]]] = Ref.make(mem)
  def get(id: Int): Task[Option[User]] =
    for {
      ref     <- storageR
      storage <- ref.get
    } yield storage.find(_.id == id)

  def create(entity: User): Task[User] = for {
    ref <- storageR
    _   <- ref.update(_ appended entity)
  } yield entity

  def delete(id: Int): Task[Unit] = for {
    ref <- storageR
    _   <- ref.update(_ filterNot (_.id == id))
  } yield ()
}

object InMemoryUserPersistence {
  val layer = ZLayer(
    ZIO.service[Vector[User]] map InMemoryUserPersistence.apply
  )
}
