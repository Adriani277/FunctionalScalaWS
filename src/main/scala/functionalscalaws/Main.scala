package functionalscalaws

import functionalscalaws.http.HttpServer
import zio._
import zio.logging._

object Main extends zio.App {
  def run(args: List[String]): zio.URIO[zio.ZEnv, ExitCode] =
    program.provideCustomLayer(Layers.live.appLayer).exitCode

  private val program: ZIO[Layers.AppEnv, Throwable, Unit] =
    for {
      _ <- log.info("Starting HTTP server")
      f <- HttpServer.make.useForever.fork
      _ <- log.info("HTTP server started")
      _ <- f.await
    } yield ()
}
