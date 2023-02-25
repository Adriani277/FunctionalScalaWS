package functionalscalaws

import functionalscalaws.http.HttpServer
import zio._
import zio.http.Server
import functionalscalaws.services.db.PaymentRepository
import io.getquill.jdbczio.Quill.H2
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.jdbczio.Quill.DataSource
import io.getquill.jdbczio.Quill
import io.getquill.SnakeCase
import functionalscalaws.http.PaymentsEndpoint

// import zio.logging._

final case class Example(name: String)

object Main extends ZIOAppDefault {

  override def run: ZIO[Any & (ZIOAppArgs & Scope), Any, Any] =
    (for {
      config <- ZIO.service[MyConfig]
      _      <- Console.printLine(config.toString)
      _      <- program
    } yield ()).provide(
      MyConfig.live,
      PaymentRepository.live,
      Quill.H2.fromNamingStrategy(SnakeCase),
      DataSource.fromPrefix("databaseConfig"),
      PaymentsEndpoint.live,
      Server.default
    )

  // program.provideCustomLayer(Layers.live.appLayer).exitCode

  private val program =
    (for {
      _ <- ZIO.logInfo("Starting HTTP server")
      _ <- ZIO.logInfo("Creating DB")
      _ <- Migrations.migrate
      // _ <- PreStartupProgram.live.flatMap(_.createTable)
      _ <- HttpServer.run
    } yield ())
    //  *> HttpServer.server.use { _ =>
    //   log.info("HTTP server started") *> ZIO.never
    // }
}
