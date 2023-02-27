package functionalscalaws

import functionalscalaws.http.HttpServer
import zio._
import zio.http.Server
import io.getquill.jdbczio.Quill.H2
import io.getquill.context.ZioJdbc.DataSourceLayer
import io.getquill.jdbczio.Quill.DataSource
import io.getquill.jdbczio.Quill
import io.getquill.SnakeCase

import functionalscalaws.configuration.MyConfig
import functionalscalaws.http.NoteEndpoints
import functionalscalaws.services.NoteService
import functionalscalaws.db.NoteRepository
import functionalscalaws.http.StatusEndpoint

object Main extends ZIOAppDefault:

  override def run: ZIO[Any & (ZIOAppArgs & Scope), Any, Any] =
    (for {
      config <- ZIO.service[MyConfig]
      _      <- Console.printLine(config.toString)
      _      <- program
    } yield ()).provide(
      MyConfig.live,
      Quill.H2.fromNamingStrategy(SnakeCase),
      DataSource.fromPrefix("databaseConfig"),
      Server.default,
      NoteEndpoints.live,
      NoteService.live,
      NoteRepository.live,
      StatusEndpoint.live
    )

  private val program =
    (for {
      _ <- ZIO.logInfo("Creating DB")
      _ <- Migrations.migrate
      _ <- ZIO.logInfo("Starting HTTP server")
      _ <- HttpServer.run
    } yield ())
