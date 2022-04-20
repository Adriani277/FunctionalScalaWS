package functionalscalaws

import functionalscalaws.persistence._
import zio._

trait UserProgramAlg {
  def getUserWithLogging(id: Int): Task[Option[User]]
}

final case class UserProgram(private val persistence: UserPersistenceAlg[User])
    extends UserProgramAlg {
  def getUserWithLogging(id: Int): Task[Option[User]] = for {
    _    <- ZIO.logInfo(s"retrieving user for id $id")
    user <- persistence.get(id)
  } yield user
}

// object UserProgram {

//   type UserProgram = Service

//   trait Service {
//     def getUserWithLogging(
//         id: Int
//     ): Task[User]
//   }

//   object Service {
//     val live: ZLayer[UserPersistence, Nothing, Service] =
//       ZLayer.fromFunction[UserPersistence, Service] { deps =>
//         new Service {
//           def getUserWithLogging(
//               id: Int
//           ): Task[User] =
//             (for {
//               // _    <- log.info(s"Retrieving User $id")
//               user <- get[User](id)
//               // _    <- log.info(s"User successfully retrieved")
//             } yield user).provide(ZLayer.succeed(deps))
//         }
//       }
//   }

//   def getUserWithLogging(id: Int): RIO[UserProgram, User] = ZIO.environmentWithZIO(_.get getUserWithLogging id)
// }
