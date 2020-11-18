package functionalscalaws

import functionalscalaws.configuration._
import functionalscalaws.logging._
import functionalscalaws.persistence._
import zio.ZLayer
import zio.clock.Clock
import zio.config.ZConfig
import zio.logging.Logging

object Layers {
  type AppEnv = Logging with ZConfig[Config] with Persistence[User]

  object live {
    private val rootLayer                        = Clock.live ++ zio.console.Console.live
    private val logLayer                         = rootLayer >>> consoleLogger
    private val persistenceLayer                 = logLayer >>> inMemory(Vector.empty)
    
    val appLayer: ZLayer[Any, Throwable, AppEnv] = logLayer ++ liveConfig ++ persistenceLayer
  }
}
