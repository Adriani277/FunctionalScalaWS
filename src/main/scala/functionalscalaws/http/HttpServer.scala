package functionalscalaws.http

import org.http4s.server.blaze.BlazeServerBuilder
import functionalscalaws.http.HelloRoutes
import org.http4s.server.blaze._
import org.http4s.implicits._
import org.http4s.server.Router
import cats.effect.ConcurrentEffect
import cats.effect.Timer

object HttpServer {
  def make[F[_]: ConcurrentEffect: Timer](uri: String, port: Int) = {
    val service = HelloRoutes[F]().helloWorldService
    val httpApp = Router("/" -> service).orNotFound

    BlazeServerBuilder[F].bindHttp(port, uri).withHttpApp(httpApp)
  }
}
