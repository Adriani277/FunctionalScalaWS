package functionalscalaws

import zio._
import zio.config.magnolia._
import zio.config.syntax._
import zio.config.typesafe._

final case class MyConfig(
    http: MyConfig.HttpConfig,
    databaseConfig: MyConfig.DatabaseConfig
)
object MyConfig {
  final case class HttpConfig(uri: String, port: Int)
  final case class DataSource(url: String, user: String)

  final case class DatabaseConfig(dataSource: DataSource)

  val live = zio.config.ZConfig
    .fromHoconFilePath("src/main/resources/application.conf", descriptor[MyConfig])
}

// package object configuration {
//   val liveConfig: ZLayer[Any, Throwable, Has[Config]] =
//     TypesafeConfig
//       .fromHoconFile(new java.io.File("src/main/resources/application.conf"), descriptor[Config]).orDie

//   val doobieConfig = liveConfig.narrow[Config.DoobieConfig](_.doobie)
// }
