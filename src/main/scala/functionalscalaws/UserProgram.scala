package functionalscalaws

import functionalscalaws.algebras.Persistence
import zio.ZIO
import functionalscalaws.algebras.Persistence._
import functionalscalaws.logging._

object UserProgram {
  def getUserWithLogging(
      id: Int
  ): ZIO[Persistence.UserPersistence with Logging, Throwable, User] =
    for {
      _    <- info(s"Retrieving User $id")
      user <- get[User](id)
      _    <- info(s"User successfully retrieved")
    } yield user
}
