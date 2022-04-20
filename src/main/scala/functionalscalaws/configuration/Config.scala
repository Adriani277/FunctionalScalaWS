package functionalscalaws.configuration

import zio.config.magnolia.descriptor
import zio.config.typesafe._

final case class Config(http: Config.HttpConfig)
object Config {
  final case class HttpConfig(uri: String, port: Int)

  val liveConfig =
    TypesafeConfig
      .fromHoconFile(new java.io.File("src/main/resources/application.conf"), descriptor[Config])
      .mapError(e => new RuntimeException(e.prettyPrint()))
}
