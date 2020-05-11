package functionalscalaws.configuration

import pureconfig._
import pureconfig.generic.auto._
import zio.{ZLayer, Has, ZIO}
import zio.blocking._

final case class Config(http: Config.HttpConfig)
object Config {
  final case class HttpConfig(uri: String, port: Int)
}

object ZConfig {
  type ZConfig = Has[ZConfig.Service]

  trait Service {
    def load: ZIO[Any, Throwable, Config]
  }

  val liveConfig: ZLayer[Blocking, Nothing, ZConfig] = ZLayer.fromService(
    block =>
      new Service {
        def load = block.effectBlocking(ConfigSource.default.loadOrThrow[Config])
      }
  )

  def load: ZIO[ZConfig with Blocking, Throwable, Config] =
    ZIO.accessM(_.get.load)

  // val testZConfig: ZLayer[Any, Nothing, ZConfig] = ZLayer.succeed(???)
}
