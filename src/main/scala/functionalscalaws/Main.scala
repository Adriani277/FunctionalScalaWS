package functionalscalaws

import cats.effect._
import cats.effect.{ExitCode, IO}
import functionalscalaws.http.HttpServer
import cats.implicits._
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import functionalscalaws.configuration.Config
import pureconfig.ConfigSource

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val config = for {
      blocker <- Blocker[IO]
      config  <- Config.make[IO](ConfigSource.default, blocker)
    } yield config

    config.use(conf => program[IO](conf).as(ExitCode.Success))
  }

  private def program[F[_]: ConcurrentEffect: Timer](config: Config) =
    for {
      logger <- Slf4jLogger.create[F]
      _      <- logger.info("Starting HTTP server")
      server <- HttpServer.make[F](config.http.uri, config.http.port).serve.compile.drain
    } yield server
}
