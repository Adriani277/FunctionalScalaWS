package functionalscalaws

import cats.effect._
import cats.effect.{ExitCode, IO}
import functionalscalaws.http.HttpServer
import cats.implicits._
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    program[IO].as(ExitCode.Success)
  }

  private def program[F[_]: ConcurrentEffect: Timer] =
    for {
      logger <- Slf4jLogger.create[F]
      _      <- logger.info("Starting HTTP server")
      server <- HttpServer.make[F]("localhost", 8080).serve.compile.drain
    } yield server
}
