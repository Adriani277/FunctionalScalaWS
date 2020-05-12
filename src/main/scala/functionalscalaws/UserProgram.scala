package functionalscalaws

import functionalscalaws.algebras.Persistence
import zio.ZIO
import functionalscalaws.algebras.Persistence._
import functionalscalaws.Logging

object UserProgram {
  def getUserWithLogging(
      id: Int
  ): ZIO[Persistence.UserPersistence with Logging.Logging, Throwable, User] =
    for {
      _    <- Logging.info(s"Retrieving User $id")
      user <- get[User](id)
      _    <- Logging.info(s"User successfully retrieved")
    } yield user
}
