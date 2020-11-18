package functionalscalaws

import java.time.OffsetDateTime

import functionalscalaws.logging.consoleLogger
import functionalscalaws.persistence
import zio.ZLayer
import zio._
import zio.test.Assertion._
import zio.test._
import zio.test.mock.Expectation._
import zio.test.mock._
object UserProgramSpec extends DefaultRunnableSpec {
  def spec = suite("UserProgram") {
    testM("getLoggedUser") {
      checkM(Gen.anyInt) { id =>
        val loggerBehaviour = MockClock.CurrentDateTime(value(OffsetDateTime.MAX)) andThen
          MockConsole
            .PutStrLn(containsString(s"Retrieving User $id"), value(())) andThen
          MockClock.CurrentDateTime(value(OffsetDateTime.MAX)) andThen
          MockConsole.PutStrLn(containsString("User successfully retrieved"), value(()))

        val mockLogger = loggerBehaviour >>> consoleLogger

        val mockPersistence: ULayer[persistence.UserPersistence] =
          PersistenceMock.Get(equalTo(id), value(persistence.User(id, "Adriani")))

        val result = UserProgram.getUserWithLogging(id).provideLayer(mockPersistence ++ mockLogger)

        assertM(result)(
          Assertion.equalTo(persistence.User(id, "Adriani"))
        )
      }
    }
  }
}

object PersistenceMock extends Mock[persistence.UserPersistence] {
  import persistence._

  object Create extends Effect[User, Nothing, User]
  object Delete extends Effect[Int, Nothing, Boolean]
  object Get    extends Effect[Int, Nothing, User]

  val compose: URLayer[Has[Proxy], UserPersistence] =
    ZLayer.fromService(
      invoke =>
        new Service[User] {
          def create(entity: User): zio.Task[User] = invoke(Create, entity)
          def delete(id: Int): zio.Task[Boolean]   = invoke(Delete, id)
          def get(id: Int): zio.Task[User]         = invoke(Get, id)
        }
    )
}
