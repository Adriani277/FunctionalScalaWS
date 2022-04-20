package functionalscalaws

import functionalscalaws.configuration.Config
import zio._
import functionalscalaws.http.HttpServer
// import zio.logging._

object Main extends zio.ZIOAppDefault {

  private val logger = zio.logging.console()

  override def hook: RuntimeConfigAspect = logger

  def run = program.provide(Config.liveConfig)
  // program.provideSomeLayer[zio.ZEnv](Layers.live.appLayer).exitCode

  private val program = for {
    _    <- ZIO.logInfo("starting")
    conf <- ZIO.service[Config]
    _    <- HttpServer.make
    _    <- ZIO.logInfo(s"Values read ${conf}")
  } yield ()
  // for {
  //   // _      <- log.info("Starting HTTP server")
  //   server <- HttpServer.make
  //   f      <- server.useForever.fork
  //   // _      <- log.info("HTTP server started")
  //   _      <- f.await
  // } yield ()
}
