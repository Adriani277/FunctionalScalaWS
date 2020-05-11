package functionalscalaws.http

import org.http4s._
import org.http4s.implicits._
import org.http4s.Uri
import zio.interop.catz._
import zio.test._
import functionalscalaws.algebras.Persistence
import zio.ZLayer
import functionalscalaws.algebras.Persistence.User

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
              .map(_.get) >>> Persistence.testPersistence
          )

          assertM(result) {
            Assertion.equalTo((Status.Ok, s"""{"id":$s,"v":"Adriani"}"""))
          }
      }
    }
  )
}
