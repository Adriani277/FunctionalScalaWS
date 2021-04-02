package functionalscalaws.http

import java.nio.charset.StandardCharsets

import org.http4s._
import zio.Task
import zio.interop.catz._
import zio.json._
import zio.test.Assertion._
import zio.test._

object ZIOJsonEntityCodecSpec extends DefaultRunnableSpec {
  def spec: ZSpec[Environment, Failure] = suite("JsonCodec")(
    suite("jsonEncoderOf")(testM("encodes json") {
      case class Test(a: String, b: Int)
      implicit val encoder = DeriveJsonEncoder.gen[Test]
      checkM(Gen.anyString, Gen.anyInt) { (s, i) =>
        val res = ZioJsonEntityCodec
          .jsonEncoderOf[Test]
          .toEntity(Test(s, i))
          .body
          .compile
          .toList
          .map(v => new String(v.toArray, StandardCharsets.UTF_8))

        assertM(res)(equalTo(s"""{"a":"$s","b":$i}"""))
      }
    }),
    suite("jsonOf")(testM("decodes json") {
      case class Test(a: String, b: Int)
      implicit val decoder = DeriveJsonDecoder.gen[Test]

      checkM(Gen.anyString, Gen.anyInt) { (s, i) =>
        val media = Request[Task]()
          .withEntity(s"""{"a":"$s","b":$i}""")
          .withHeaders(Header("Content-Type", "application/json"))

        assertM(ZioJsonEntityCodec.jsonOf[Task, Test].decode(media, true).value)(
          isRight(equalTo(Test(s, i)))
        )
      }
    })
  )
}
