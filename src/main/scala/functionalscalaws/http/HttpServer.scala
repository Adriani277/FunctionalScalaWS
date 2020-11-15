package functionalscalaws.http

import functionalscalaws.Config
import functionalscalaws.Layers
import functionalscalaws.http.HelloRoutes
import functionalscalaws.persistence.UserPersistenceRIO
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.blaze._
import zio.ZIO
import zio.clock.Clock
import zio.config._
import zio.interop.catz._

object HttpServer {
  val make = for {
    config <- getConfig[Config]
    service = HelloRoutes.helloWorldService
    httpApp = Router("/" -> service).orNotFound
    blaze <- ZIO.runtime[Layers.AppEnv with Clock].flatMap { implicit CE =>
      ZIO.effect(
        BlazeServerBuilder[UserPersistenceRIO]
          .bindHttp(config.http.port, config.http.uri)
          .withHttpApp(httpApp)
          .resource
          .toManagedZIO
      )
    }
  } yield blaze
}
