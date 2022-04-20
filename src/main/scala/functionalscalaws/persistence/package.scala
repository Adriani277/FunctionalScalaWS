// package functionalscalaws

// import io.circe.generic.semiauto._
// import izumi.reflect.Tag
// import zio._
// import zio.Clock
// // import zio.logging._

// package object persistence {
//   final case class User(id: Int, v: String)
//   object User {
//     implicit val encoder = deriveEncoder[User]
//     implicit val decoder = deriveDecoder[User]
//   }

//   type UserPersistenceRIO[A] = RIO[Persistence[User] with Clock, A]
//   type Persistence[A]        = Service[A]
//   type UserPersistence       = Service[User]

//   trait Service[A] {
//     def get(id: Int): Task[A]
//     def create(entity: User): Task[A]
//     def delete(id: Int): Task[Boolean]
//   }

//   def inMemory(storage: Vector[User]) = ZLayer.fromZIO(
//     // log.info("Starting in-memory repository") *>
//       Ref.make(storage).map { mem =>
//         new Service[User] {
//           def get(id: Int): zio.Task[User] =
//             mem.get.flatMap(
//               users =>
//                 Task
//                   .someOrFail(new RuntimeException("Not found"))(Task.succeed(users.find(_.id == id)))
//             )

//           def create(entity: User): zio.Task[User] =
//             mem.update(_ :+ entity).map(_ => entity)

//           def delete(id: Int): zio.Task[Boolean] =
//             mem.modify(mem => true -> mem.filterNot(_.id == id))
//         }
//       }
//   )

//   def get[A: Tag](id: Int): ZIO[Persistence[A], Throwable, A] =
//     ZIO.environmentWithZIO(_.get.get(id))

//   def create[A: Tag](entity: User) =
//     ZIO.environmentWithZIO[Persistence[A]](_.get.create(entity))

//   def delete[A: Tag](id: Int) =
//     ZIO.environmentWithZIO[Persistence[A]](_.get.delete(id))
// }
