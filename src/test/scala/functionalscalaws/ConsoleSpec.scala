package functionalscalaws

import org.scalatest.funspec.AnyFunSpec
import cats.effect.concurrent.Ref
import cats.effect.IO
import cats.implicits._
import org.scalatest.matchers.should.Matchers

final class ConsoleSpec extends AnyFunSpec with Matchers {
  describe("Console") {
    it("putStrLn puts a string into the buffer") {
      Ref
        .of[IO, List[String]](List.empty)
        .flatMap { ref =>
          val underTest = testConsole(ref)

          underTest.putStrLn("Hi") *>
            ref.get.map { b =>
              b.head shouldBe "Hi"
            }
        }
        .unsafeRunSync()
    }

    it("getStrLn gets a string from the buffer") {
      Ref
        .of[IO, List[String]](List("World"))
        .flatMap { ref =>
          val underTest = testConsole(ref)

          underTest.getStrLn().map { result =>
            result shouldBe "World"
          }
        }
        .unsafeRunSync()
    }
  }

  private def testConsole(buffer: Ref[IO, List[String]]) = new functionalscalaws.algebras.Console[IO] {
    def getStrLn(): IO[String]        = buffer.get.map(_.mkString)
    def putStrLn(s: String): IO[Unit] = buffer.set(List(s))
  }
}
