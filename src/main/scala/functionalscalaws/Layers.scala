package functionalscalaws

import functionalscalaws.configuration._
import functionalscalaws.logging._
import functionalscalaws.persistence._
import zio.ZLayer
import zio.blocking.Blocking

object Layers {
  type Layer0 = Logging with Blocking
  type Layer1 = Layer0 with ZConfig with Persistence[User]
  type AppEnv = Layer1

  object live {
    val layer0: ZLayer[Blocking, Throwable, Layer0]   = Blocking.any ++ logLogger
    val layer1: ZLayer[Layer0, Throwable, Layer1]     = liveConfig ++ inMemory(Vector.empty) ++ ZLayer.identity
    val appLayer: ZLayer[Blocking, Throwable, AppEnv] = layer0 >>> layer1
  }
}
