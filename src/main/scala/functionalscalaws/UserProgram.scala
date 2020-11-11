package functionalscalaws

import functionalscalaws.logging._
import functionalscalaws.persistence._
import zio.ZIO

object UserProgram {
  def getUserWithLogging(
      id: Int
  ): ZIO[UserPersistence with Logging, Throwable, User] =
    for {
      _    <- info(s"Retrieving User $id")
      user <- get[User](id)
      _    <- info(s"User successfully retrieved")
    } yield user

  def getUserWithoutLogging(id: Int): ZIO[UserPersistence, Throwable, User] =
    get[User](id)
}
