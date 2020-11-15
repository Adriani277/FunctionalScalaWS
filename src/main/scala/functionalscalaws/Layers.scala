package functionalscalaws

import functionalscalaws.configuration._
import functionalscalaws.logging._
import functionalscalaws.persistence._
import zio.ZLayer
import zio.config.ZConfig

object Layers {
  type AppEnv = Logging with ZConfig[Config] with Persistence[User]

  object live {
    val appLayer: ZLayer[Any, Throwable, AppEnv] = liveConfig ++ logLogger ++ (logLogger >>> inMemory(Vector.empty))
  }
}
