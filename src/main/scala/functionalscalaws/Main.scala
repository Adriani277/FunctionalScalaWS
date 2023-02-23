package functionalscalaws

import functionalscalaws.http.HttpServer
import functionalscalaws.program.PreStartupProgram
import zio._
import zio.http.Server
import functionalscalaws.program.PreStartupProgramAlg
import functionalscalaws.services.db.PaymentRepository
import io.getquill.jdbczio.Quill.H2
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.jdbczio.Quill.DataSource
import io.getquill.jdbczio.Quill
import io.getquill.SnakeCase

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
      DataSource.fromPrefix("databaseConfig")
    )

  // program.provideCustomLayer(Layers.live.appLayer).exitCode

  private val program: ZIO[PaymentRepository, Throwable, Unit] =
    (for {
      _ <- ZIO.logInfo("Starting HTTP server")
      _ <- ZIO.logInfo("Creating DB")
      _ <- PreStartupProgram.live.map(_.createTable)
      _ <- HttpServer.server.provide(Server.default)
    } yield ())
    //  *> HttpServer.server.use { _ =>
    //   log.info("HTTP server started") *> ZIO.never
    // }
}
