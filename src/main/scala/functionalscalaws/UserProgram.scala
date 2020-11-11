package functionalscalaws

import zio.ZIO
import functionalscalaws.persistence._
import functionalscalaws.logging._

object UserProgram {
  def getUserWithLogging(
      id: Int
  ): ZIO[UserPersistence with Logging, Throwable, User] =
    for {
      _    <- info(s"Retrieving User $id")
      user <- get[User](id)
      _    <- info(s"User successfully retrieved")
    } yield user
}
