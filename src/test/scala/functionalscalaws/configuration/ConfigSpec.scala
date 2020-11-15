package functionalscalaws.configuration

import functionalscalaws.Config
import functionalscalaws.configuration._
import zio.config._
import zio.test._

object ConfigSpec extends DefaultRunnableSpec {
  def spec = suite("config") {
    testM("getConf") {
      assertM(getConfig[Config])(
        Assertion.isSubtype[Config](Assertion.anything)
      ).provideLayer(liveConfig)
    }
  }
}
