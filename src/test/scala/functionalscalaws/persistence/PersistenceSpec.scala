package functionalscalaws.persistence

import functionalscalaws.persistence._
import zio._
import zio.logging.LogContext
import zio.test.Assertion._
import zio.test._

object PersistenceSpec extends DefaultRunnableSpec {
  val userGen = for {
    id  <- Gen.anyInt
    str <- Gen.alphaNumericString
  } yield User(id, str)

  val usersGen = Gen.vectorOfBounded(1, 10)(userGen)

  def spec = suite("inMemory")(getTests, createTests)

  val getTests = suite("get") {
    testM("returns error when user does not exist") {
      checkM(Gen.anyInt) { id =>
        assertM(get[User](id).run)(
          fails(isSubtype[RuntimeException](anything))
        ).provideLayer(testLogger >>> inMemory(Vector.empty))
      }
    }

    testM("returns a user when id exists") {
      checkM(usersGen) { users =>
        assertM(get[User](users.head.id))(
          equalTo(users.head)
        ).provideLayer(testLogger >>> inMemory(users))
      }
    }
  }

  val createTests = suite("create") {
    testM("creates a new user") {
      checkM(userGen) { user =>
        assertM(create[User](user))(
          equalTo(user)
        ).provideLayer(testLogger >>> inMemory(Vector.empty))
      }
    }

    testM("updates existing user") {
      checkM(userGen, userGen) { (user1, user2) =>
        val newUser = user2.copy(user1.id)
        assertM(create[User](user1) *> create[User](newUser))(
          equalTo(newUser)
        ).provideLayer(testLogger >>> inMemory(Vector.empty))
      }
    }
  }

  private val testLogger = ZLayer.fromEffect {
    UIO.succeed(new zio.logging.Logger[String] {
      def locally[R1, E, A1](f: LogContext => LogContext)(zio: ZIO[R1, E, A1]): ZIO[R1, E, A1] = zio
      def log(line: => String): zio.UIO[Unit]                                                  = UIO.unit
      def logContext: zio.UIO[LogContext]                                                      = UIO(LogContext.empty)
    })
  }
}
