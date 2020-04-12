package functionalscalaws

import org.scalatest.funspec.AnyFunSpec
import cats.effect.concurrent.Ref
import cats.effect.IO
import cats.implicits._
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalacheck.Gen

final class ConsoleSpec extends AnyFunSpec with Matchers with ScalaCheckPropertyChecks {
  describe("Console") {
    it("putStrLn puts a string into the buffer") {
      forAll(Gen.alphaNumStr) { str =>
        Ref
          .of[IO, List[String]](List.empty)
          .flatMap { ref =>
            val underTest = testConsole(ref)

            underTest.putStrLn(str) *>
              ref.get.map { b =>
                b.head shouldBe str
              }
          }
          .unsafeRunSync()
      }
    }

    it("getStrLn gets a string from the buffer") {
      forAll(Gen.nonEmptyListOf(Gen.alphaNumStr)) { buffer =>
        Ref
          .of[IO, List[String]](buffer)
          .flatMap { ref =>
            val underTest = testConsole(ref)

            underTest.getStrLn().map { result =>
              result shouldBe buffer.mkString
            }
          }
          .unsafeRunSync()
      }
    }
  }

  private def testConsole(buffer: Ref[IO, List[String]]) =
    new functionalscalaws.algebras.Console[IO] {
      def getStrLn(): IO[String]        = buffer.get.map(_.mkString)
      def putStrLn(s: String): IO[Unit] = buffer.set(List(s))
    }
}
