package functionalscalaws

import zio.Task
import zio.ZLayer
import zio.ZRef
import zio.ZIO
import zio.Has
import zio.RIO
import io.circe.generic.semiauto._
import zio.clock.`package`.Clock
import zio.`package`.Ref
import izumi.reflect.Tag

package object persistence {
  final case class User(id: Int, v: String)
  object User {
    implicit val encoder = deriveEncoder[User]
    implicit val decoder = deriveDecoder[User]
  }

  type UserPersistenceRIO[A] = RIO[Persistence[User] with Clock, A]
  type Persistence[A]        = Has[Service[A]]
  type UserPersistence       = Has[Service[User]]

  trait Service[A] {
    def get(id: Int): Task[A]
    def create(entity: User): Task[A]
    def delete(id: Int): Task[Boolean]
  }

  def inMemory = ZLayer.fromEffect(
    ZRef.make(Vector.empty[User]).map { mem =>
      new Service[User] {
        def get(id: Int): zio.Task[User] =
          mem.get.flatMap(
            users =>
              Task.require(new RuntimeException("Not found"))(Task.succeed(users.find(_.id == id)))
          )

        def create(entity: User): zio.Task[User] =
          mem.update(_ :+ entity).map(_ => entity)

        def delete(id: Int): zio.Task[Boolean] =
          mem.modify(mem => true -> mem.filterNot(_.id == id))
      }
    }
  )

  def testPersistence =
    ZLayer.fromFunction[Ref[Vector[User]], Service[User]](
      mem =>
        new Service[User] {
          def get(id: Int): zio.Task[User] =
            mem.get.flatMap(
              users =>
                Task
                  .require(new RuntimeException("Not found"))(Task.succeed(users.find(_.id == id)))
            )

          def create(entity: User): zio.Task[User] =
            mem.update(_ :+ entity).map(_ => entity)

          def delete(id: Int): zio.Task[Boolean] =
            mem.modify(mem => true -> mem.filterNot(_.id == id))
        }
    )

  def get[A: Tag](id: Int): ZIO[Persistence[A], Throwable, A] =
    ZIO.accessM(_.get.get(id))

  def create[A: Tag](entity: User) =
    ZIO.accessM[Persistence[A]](_.get.create(entity))

  def delete[A: Tag](id: Int) =
    ZIO.accessM[Persistence[A]](_.get.delete(id))
}
