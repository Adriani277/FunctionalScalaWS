package functionalscalaws

import zio.test.DefaultRunnableSpec
import zio.test._
import zio.test.Assertion._
import zio.Task
import zio.json._
import org.http4s._
import zio.interop.catz._
import java.nio.charset.StandardCharsets
import java.nio.ByteBuffer
import org.http4s.jawn.JawnInstances

object ZEncoder extends DefaultRunnableSpec {
  final case class Test(name: String)
  object Test {
    implicit val encoder: JsonEncoder[Test] = DeriveJsonEncoder.gen[Test]
    implicit val decoder: JsonDecoder[Test] = DeriveJsonDecoder.gen[Test]
  }

  def spec = suite("EntityEncoder") {
    testM("encodes") {
      val media = Request[Task]()
        .withEntity(Test("john").toJson)
        .withHeaders(Header("Content-Type", "application/json"))
      val dec1 = EntityDecoder.decodeString(media).map(_.fromJson[Test])
      val dec2 = EntityDecoder
        .byteArrayDecoder[Task]
        .flatMapR { b =>
          val json = new String(b, StandardCharsets.UTF_8).fromJson[Test]
          val res  = Task.succeed(json.fold(s => Left(InvalidMessageBodyFailure(s, None)), Right(_)))

          DecodeResult[Task, Test](res)
        }
        .decode(media, true)
        .value

      def genDecoder[A: JsonDecoder] =
        EntityDecoder.decodeBy[Task, A](MediaType.application.json) { m =>
          EntityDecoder.collectBinary(m).subflatMap { chunk =>
            val str = new String(chunk.toArray, StandardCharsets.UTF_8)
            if (str.nonEmpty)
              str.fromJson[A].fold(e => Left(MalformedMessageBodyFailure(e, None)), Right(_))
            else
              Left(MalformedMessageBodyFailure("Invalid JSON: empty body")) // Use JawnInstance.defaultEmptyBody
          }
        }

      def genEncoder[A: JsonEncoder] =
        EntityEncoder.stringEncoder[Task].contramap[A](_.toJson)

      val res = genDecoder[Test].decode(media, true).value

      genEncoder[Test]
        .toEntity(Test("John"))
        .body
        .compile
        .toList
        .map(v => new String(v.toArray, StandardCharsets.UTF_8))
        .map(println) *>
        assertM(res)(isRight(equalTo(Test("john"))))
    }
  }
}
