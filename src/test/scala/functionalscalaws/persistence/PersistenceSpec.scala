package functionalscalaws.persistence

import functionalscalaws.persistence._
import zio.test.Assertion._
import zio.test._

object PersistenceSpec extends DefaultRunnableSpec {
  val userGen = for {
    id  <- Gen.anyInt
    str <- Gen.alphaNumericString
  } yield User(id, str)

  val usersGen = Gen.vectorOfBounded(1, 10)(userGen)

  def spec = suite("inMemory") {
    suite("get") {
      testM("returns error when user does not exist") {
        checkM(Gen.anyInt) { id =>
          assertM(get[User](id).run)(
            fails(isSubtype[RuntimeException](anything))
          ).provideLayer(inMemory(Vector.empty))
        }
      }

      testM("returns a user when id exists") {
        checkM(usersGen) { users =>
          assertM(get[User](users.head.id))(
            equalTo(users.head)
          ).provideLayer(inMemory(users))
        }
      }
    }
  }
}
