package functionalscalaws

import functionalscalaws.logging._
import zio.blocking.Blocking
import functionalscalaws.configuration.ZConfig
import functionalscalaws.algebras.Persistence
import zio.ZLayer
import functionalscalaws.algebras.Persistence.User

object Layers {
  type Layer0 = Logging with Blocking
  type Layer1 = Layer0 with ZConfig.ZConfig with Persistence.Persistence[User]
  type AppEnv = Layer1

  object live {
    val layer0: ZLayer[Blocking, Throwable, Layer0] = Blocking.any ++ logLogger
    val layer1
        : ZLayer[Layer0, Throwable, Layer1]           = ZConfig.liveConfig ++ Persistence.inMemory ++ ZLayer.identity
    val appLayer: ZLayer[Blocking, Throwable, AppEnv] = layer0 >>> layer1
  }
}
