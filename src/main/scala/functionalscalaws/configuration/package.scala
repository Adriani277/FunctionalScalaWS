package functionalscalaws

import zio._
import zio.config._
import zio.config.magnolia.DeriveConfigDescriptor.descriptor
import zio.config.typesafe._

final case class Config(http: Config.HttpConfig)
object Config {
  final case class HttpConfig(uri: String, port: Int)
}

package object configuration {
  val liveConfig: ZLayer[Any, Throwable, ZConfig[Config]] =
    TypesafeConfig
      .fromHoconFile(new java.io.File("src/main/resources/application.conf"), descriptor[Config])
      .mapError(e => new RuntimeException(e.prettyPrint()))
}
