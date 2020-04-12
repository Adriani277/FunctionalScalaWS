package functionalscalaws.http

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import cats.effect.IO
import org.http4s.implicits._
import org.http4s.Request
import org.http4s.Method
import org.http4s.Status

final class HelloRoutesSpec extends AnyFunSpec with Matchers {
  describe("HelloRoutes") {
    it("returns Ok") {
      val response = HelloRoutes[IO]().helloWorldService.orNotFound
        .run(
          Request(method = Method.GET, uri = uri"/hello/Adriani")
        )
        .unsafeRunSync

      response.status shouldBe Status.Ok
      response.as[String].unsafeRunSync shouldBe "Hello, Adriani"
    }
  }
}
