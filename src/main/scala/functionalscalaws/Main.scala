package functionalscalaws

import functionalscalaws.http.HttpServer
import zio.ZIO
import zio.interop.catz._
import zio.clock.`package`.Clock

object Main extends CatsApp {
  def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    program.provideSomeLayer[zio.ZEnv](Layers.live.appLayer).fold(_ => 1, _ => 0)
  }

  private val program: ZIO[Layers.AppEnv with Clock, Throwable, Unit] =
    for {
      _      <- Logging.info("Starting HTTP server")
      blaze  <- HttpServer.make
      server <- blaze.serve.compile.drain
    } yield server
}
