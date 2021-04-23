package functionalscalaws.http
import zhttp.http._
import zio.test._

object StatusEndpointSpec extends DefaultRunnableSpec {
  def spec = suite("HelloRoutesZIO")(
    testM("returns Ok response for /hello/<name> endpoint") {
      val request  = Request((Method.GET, URL(Path("/status"))))
      val response = StatusEndpoint.route.evalAsEffect(request)

      assertM(response) {
        Assertion.equalTo(Response.jsonString("""{"status":"ok"}"""))
      }
    }
  )
}
