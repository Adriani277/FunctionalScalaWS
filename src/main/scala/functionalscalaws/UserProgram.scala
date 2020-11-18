package functionalscalaws

import functionalscalaws.persistence._
import zio.ZIO
import zio.logging._

object UserProgram {
  def getUserWithLogging(
      id: Int
  ): ZIO[UserPersistence with Logging, Throwable, User] =
    for {
      _    <- log.info(s"Retrieving User $id")
      user <- get[User](id)
      _    <- log.info(s"User successfully retrieved")
    } yield user
}
