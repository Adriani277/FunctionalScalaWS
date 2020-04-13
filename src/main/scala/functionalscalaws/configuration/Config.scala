package functionalscalaws.configuration

import cats.effect.{ContextShift, Blocker}
import pureconfig.ConfigSource
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.module.catseffect.syntax._
import cats.effect.Sync
import cats.effect.Resource

final case class Config(http: Config.HttpConfig)
object Config {
  final case class HttpConfig(uri: String, port: Int)

  def make[F[_]: Sync: ContextShift](configSource: ConfigSource, blocker: Blocker) =
    Resource.liftF(configSource.loadF[F, Config](blocker))
}
