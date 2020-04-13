package functionalscalaws.configuration

import org.scalatest.funspec.AnyFunSpec
import functionalscalaws.configuration.Config
import cats.effect.IO
import pureconfig.ConfigSource
import cats.effect.Blocker
import scala.concurrent.ExecutionContext
import org.scalatest.matchers.should.Matchers

class ConfigSpec extends AnyFunSpec with Matchers {
  describe("Config") {
    it("Loads the config if config file is correct") {
      implicit val cs = IO.contextShift(ExecutionContext.global)
      Blocker[IO]
        .flatMap(Config.make[IO](ConfigSource.default, _))
        .use(IO.pure)
        .unsafeRunSync() shouldBe Config(Config.HttpConfig("localhost", 8080))
    }
  }
}
