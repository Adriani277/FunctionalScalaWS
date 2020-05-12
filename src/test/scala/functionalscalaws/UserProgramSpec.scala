package functionalscalaws

import zio.test._
import functionalscalaws.algebras.Persistence.User
import zio.ZLayer
import functionalscalaws.algebras.Persistence
import functionalscalaws.Logging
import zio.test.mock.Method
import zio._
import zio.test._
import zio.test.Assertion._
import zio.test.mock.Expectation._
import functionalscalaws.LoggingMock.Info
import functionalscalaws.PersistenceMock.Get
object UserProgramSpec extends DefaultRunnableSpec {
  def spec = suite("UserProgram") {
    testM("getLoggedUser") {
      checkM(Gen.anyInt) { id => 
        val mockEnv: ULayer[Logging.Logging] = (
          (Info(equalTo(s"Retrieving User $id")) returns unit) andThen
            (Info(equalTo("User successfully retrieved")) returns unit)
        )

        val mockPersistence: ULayer[Persistence.UserPersistence] = (
          (Get(equalTo(id)) returns value(User(id, "Adriani")))
        )

        val result = UserProgram.getUserWithLogging(id).provideLayer(mockPersistence ++ mockEnv)

        assertM(result)(
          Assertion.equalTo(User(id, "Adriani"))
        )
      }
    }
  }
}

import zio._
import zio.test.mock._
object LoggingMock {
  sealed trait Tag[I, A] extends Method[Logging.Logging, I, A] {
    def envBuilder: URLayer[Has[Proxy], Logging.Logging] =
      LoggingMock.envBuilder
  }

  object Info  extends Tag[String, Unit]
  object Error extends Tag[String, Unit]

  private val envBuilder: URLayer[Has[Proxy], Logging.Logging] =
    ZLayer.fromService(
      invoke =>
        new Logging.Service {
          def info(s: String): zio.UIO[Unit]  = invoke(Info, s)
          def error(s: String): zio.UIO[Unit] = invoke(Error, s)
        }
    )
}

object PersistenceMock {
  sealed trait Tag[I, A] extends Method[Persistence.UserPersistence, I, A] {
    def envBuilder: URLayer[Has[Proxy], Persistence.UserPersistence] =
      PersistenceMock.envBuilder
  }

  object Create extends Tag[User, User]
  object Delete extends Tag[Int, Boolean]
  object Get    extends Tag[Int, User]

  private val envBuilder: URLayer[Has[Proxy], Persistence.UserPersistence] =
    ZLayer.fromService(
      invoke =>
        new Persistence.Service[User] {
          def create(entity: User): zio.Task[User] = invoke(Create, entity)
          def delete(id: Int): zio.Task[Boolean]   = invoke(Delete, id)
          def get(id: Int): zio.Task[User]         = invoke(Get, id)
        }
    )
}
