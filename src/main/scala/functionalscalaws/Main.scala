package functionalscalaws

import functionalscalaws.http.HttpServer
import functionalscalaws.logging._
import zio.ExitCode
import zio.ZIO
import zio.clock.Clock
import zio.interop.catz._

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
