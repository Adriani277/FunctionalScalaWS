package functionalscalaws.http

import zhttp.http._
import zio._
import zio.json._

object StatusEndpoint {
  case class Test(name: String, surname: String)
  implicit val codec = DeriveJsonCodec.gen[Test]

  val route = Http.collectM {
    case Method.GET -> Root / "status" => ZIO.succeed(Response.jsonString("""{"status":"ok"}"""))
    case r @ Method.POST -> Root / "greet" =>
      for {
        bodyS <- ZIO
          .fromOption(r.getBodyAsString)
          .orElseFail(new RuntimeException("Could not read body"))
        body <- ZIO.fromEither(bodyS.fromJson[Test]).mapError(new RuntimeException(_))
      } yield Response.text(s"Have received [$body]")
  }
}
