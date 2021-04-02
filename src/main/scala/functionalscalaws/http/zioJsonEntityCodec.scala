package functionalscalaws.http

import java.nio.charset.StandardCharsets

import cats.effect.Sync
import org.http4s._
import zio._
import zio.json._

object ZioJsonEntityCodec {
  def jsonOf[F[_]: Sync, A: JsonDecoder] =
    EntityDecoder.decodeBy[F, A](MediaType.application.json) { m =>
      EntityDecoder.collectBinary(m).subflatMap { chunk =>
        val str = new String(chunk.toArray, StandardCharsets.UTF_8)
        if (str.nonEmpty)
          str.fromJson[A].fold(e => Left(MalformedMessageBodyFailure(e, None)), Right(_))
        else
          Left(MalformedMessageBodyFailure("Invalid JSON: empty body"))
      }
    }

  def jsonEncoderOf[A: JsonEncoder] =
    EntityEncoder.stringEncoder[Task].contramap[A](_.toJson)
}
