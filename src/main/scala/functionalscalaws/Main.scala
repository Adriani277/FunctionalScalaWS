package functionalscalaws

import functionalscalaws.http.HttpServer
import functionalscalaws.logging._
import zio.ZIO
import zio.interop.catz._
import zio.clock.`package`.Clock
import zio.ExitCode

object Main extends zio.App {
  def run(args: List[String]): zio.URIO[zio.ZEnv, ExitCode] =
    program.provideSomeLayer[zio.ZEnv](Layers.live.appLayer).exitCode

  private val program: ZIO[Layers.AppEnv with Clock, Throwable, Unit] =
    for {
      _      <- info("Starting HTTP server")
      blaze  <- HttpServer.make
      server <- blaze.serve.compile.drain
    } yield server
}
