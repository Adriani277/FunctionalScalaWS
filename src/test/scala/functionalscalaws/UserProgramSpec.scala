package functionalscalaws

import zio.test._
import zio.ZLayer
import functionalscalaws.persistence
import functionalscalaws.logging._
import zio._
import zio.test._
import zio.test.mock._
import zio.test.Assertion._
import zio.test.mock.Expectation._
object UserProgramSpec extends DefaultRunnableSpec {
  def spec = suite("UserProgram") {
    testM("getLoggedUser") {
      checkM(Gen.anyInt) { id =>
        val mockEnv: ULayer[Logging] = LoggingMock.Info(
          equalTo(s"Retrieving User $id"),
          unit
        ) andThen
          LoggingMock.Info(equalTo("User successfully retrieved"), unit)

        val mockPersistence: ULayer[persistence.UserPersistence] =
          PersistenceMock.Get(equalTo(id), value(persistence.User(id, "Adriani")))

        val result = UserProgram.getUserWithLogging(id).provideLayer(mockPersistence ++ mockEnv)

        assertM(result)(
          Assertion.equalTo(persistence.User(id, "Adriani"))
        )
      }
    }
  }
}

object LoggingMock extends Mock[Logging] {

  object Info  extends Effect[String, Nothing, Unit]
  object Error extends Effect[String, Nothing, Unit]

  val compose: zio.URLayer[Has[Proxy], Logging] =
    ZLayer.fromService(
      invoke =>
        new Service {
          def info(s: String): zio.UIO[Unit]  = invoke(Info, s)
          def error(s: String): zio.UIO[Unit] = invoke(Error, s)
        }
    )
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
