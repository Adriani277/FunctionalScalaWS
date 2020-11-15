package functionalscalaws

import functionalscalaws.http.HttpServer
import functionalscalaws.logging._
import zio._
import zio.clock.Clock

object Main extends zio.App {
  def run(args: List[String]): zio.URIO[zio.ZEnv, ExitCode] =
    program.provideSomeLayer[zio.ZEnv](Layers.live.appLayer).exitCode

  private val program: ZIO[Layers.AppEnv with Clock, Throwable, Unit] =
    for {
      _      <- info("Starting HTTP server")
      server <- HttpServer.make
      f      <- server.useForever.fork
      _      <- info("HTTP server started")
      _      <- f.await
    } yield ()
}
