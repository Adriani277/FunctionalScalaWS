package functionalscalaws

import zio.Scope

import zio.ZIO

import zio.ZIOAppArgs
import org.flywaydb.core.Flyway

object Migrations {

  val migrate: ZIO[MyConfig, Throwable, Unit] = for {
    config <- ZIO.service[MyConfig]
    _ <- ZIO.attemptBlocking(
      Flyway
        .configure()
        .dataSource(config.databaseConfig.dataSource.url, config.databaseConfig.dataSource.user, "")
        .load()
        .migrate()
    )
  } yield ()

}
