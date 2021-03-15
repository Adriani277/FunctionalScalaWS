package functionalscalaws.http

import cats.data.Kleisli
import cats.implicits._
import functionalscalaws.Config
import functionalscalaws.Layers.AppEnv
import org.http4s.Request
import org.http4s.Response
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import zio._
import zio.config._
import zio.interop.catz._

object HttpServer {
  type ServerRIO[A] = RIO[AppEnv, A]
  type ServerRoutes = Kleisli[ServerRIO, Request[ServerRIO], Response[ServerRIO]]

  private val routes: ServerRoutes = {
    val statusRoute   = new StatusEndpoint[AppEnv].routes
    val paymentsRoute = new PaymentsEndpoint[AppEnv].routes

    val allRoutes = (statusRoute <+> paymentsRoute)

    Router("/" -> allRoutes).orNotFound
  }

  val make = for {
    config <- getConfig[Config].toManaged_
    blaze <- ZIO.runtime[AppEnv].toManaged_.flatMap { implicit rts =>
      BlazeServerBuilder[ServerRIO]
        .bindHttp(config.http.port, config.http.uri)
        .withHttpApp(routes)
        .resource
        .toManagedZIO
    }
  } yield blaze
}
