package functionalscalaws

import functionalscalaws.persistence._
import zio.Has
import zio.RIO
import zio.Task
import zio.ZIO
import zio.ZLayer
import zio.logging._

object UserProgram {

  type UserProgram = Has[Service]

  trait Service {
    def getUserWithLogging(
        id: Int
    ): Task[User]
  }

  object Service {
    val live: ZLayer[UserPersistence with Logging, Nothing, Has[Service]] =
      ZLayer.fromFunction[UserPersistence with Logging, Service] { deps =>
        new Service {
          def getUserWithLogging(
              id: Int
          ): Task[User] =
            (for {
              _    <- log.info(s"Retrieving User $id")
              user <- get[User](id)
              _    <- log.info(s"User successfully retrieved")
            } yield user).provide(deps)
        }
      }
  }

  def getUserWithLogging(id: Int): RIO[UserProgram, User] = ZIO.accessM(_.get getUserWithLogging id)
}
