package functionalscalaws

import zio._
import zio.config._
import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe._
import functionalscalaws.Config.DoobieConfig
import zio.config.syntax._

final case class Config(http: Config.HttpConfig, doobie: DoobieConfig)
object Config {
  final case class HttpConfig(uri: String, port: Int)
  final case class DoobieConfig(
      driver: String,
      url: String,
      user: String,
      password: String,
      connectionPoolSize: Int
  )
}

package object configuration {
  val liveConfig: ZLayer[Any, Throwable, ZConfig[Config]] =
    TypesafeConfig
      .fromHoconFile(new java.io.File("src/main/resources/application.conf"), descriptor[Config])
      .mapError(e => new RuntimeException(e.prettyPrint()))

  val doobieConfig = liveConfig.narrow[Config.DoobieConfig](_.doobie)
}
