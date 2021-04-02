package functionalscalaws.http
import org.http4s.Uri
import org.http4s._
import org.http4s.implicits._
import zio.interop.catz._
import zio.test._

object StatusEndpointSpec extends DefaultRunnableSpec {
  def spec = suite("HelloRoutesZIO")(
    testM("returns Ok response for /hello/<name> endpoint") {
      val uri = Uri.unsafeFromString(s"/status")
      val response = new StatusEndpoint[Any].routes.orNotFound.run(
        Request(method = Method.GET, uri = uri)
      )

      val result = for {
        resp     <- response
        body     <- resp.as[String]
      } yield (resp.status, body)

      assertM(result) {
        Assertion.equalTo((Status.Ok, s"""{"status":"ok"}"""))
      }
    }
  )
}
