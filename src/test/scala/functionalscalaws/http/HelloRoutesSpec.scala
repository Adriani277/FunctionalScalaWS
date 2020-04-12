package functionalscalaws.http

import cats.effect.IO
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.http4s._
import org.http4s.implicits._
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.scalacheck.Gen
import org.http4s.Uri

final class HelloRoutesSpec extends AnyFunSpec with Matchers with ScalaCheckDrivenPropertyChecks {
  describe("HelloRoutes") {
    it("returns Ok response for /hello/<name> endpoint") {
      forAll(Gen.alphaStr) { str =>
        val uri = Uri.unsafeFromString(s"/hello/$str")
        val response = HelloRoutes[IO]().helloWorldService.orNotFound
          .run(
            Request(method = Method.GET, uri = uri)
          )
          .unsafeRunSync

        response.status shouldBe Status.Ok
        response.as[String].unsafeRunSync shouldBe s"Hello, $str"
      }
    }
  }
}
