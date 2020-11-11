package functionalscalaws.configuration

import functionalscalaws.configuration._
import zio._
import zio.test._
import functionalscalaws.Config

object ConfigSpec extends DefaultRunnableSpec {
  def spec = suite("config") {
    testM("load") {
      assertM(load.provideLayer(liveConfig ++ blocking.Blocking.live))(
        Assertion.isSubtype[Config](Assertion.anything)
      )
    }
  }
}
