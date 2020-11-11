package functionalscalaws.http

import functionalscalaws.persistence._
import org.http4s.Uri
import org.http4s._
import org.http4s.implicits._
import zio.ZLayer
import zio.interop.catz._
import zio.test._

object AllSuites extends DefaultRunnableSpec {
  def spec = suite("All tests")(helloSuite)

  val helloSuite = suite("HelloRoutesZIO")(
    testM("returns Ok response for /hello/<name> endpoint") {
      checkM(zio.test.Gen.anyInt) {
        s =>
          val uri = Uri.unsafeFromString(s"/hello/$s")
          val response = HelloRoutes.helloWorldService.orNotFound
            .run(
              Request(method = Method.GET, uri = uri)
            )

          val test = for {
            resp <- response
            body <- resp.as[String]
          } yield (resp.status, body)

          val result = test.provideCustomLayer(
            ZLayer
              .fromEffect(zio.Ref.make(Vector(User(s, "Adriani"))))
              .map(_.get) >>> testPersistence
          )

          assertM(result) {
            Assertion.equalTo((Status.Ok, s"""{"id":$s,"v":"Adriani"}"""))
          }
      }
    }
  )
}
