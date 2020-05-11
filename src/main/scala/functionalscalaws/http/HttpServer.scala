package functionalscalaws.http

import org.http4s.server.blaze.BlazeServerBuilder
import functionalscalaws.http.HelloRoutes
import org.http4s.server.blaze._
import org.http4s.implicits._
import org.http4s.server.Router
import zio.interop.catz._
import zio.ZIO
import functionalscalaws.configuration.ZConfig
import functionalscalaws.algebras.Persistence
import zio.clock.`package`.Clock
import functionalscalaws.Layers

object HttpServer {
  val make = for {
    config <- ZConfig.load
    service = HelloRoutes.helloWorldService
    httpApp = Router("/" -> service).orNotFound
    blaze <- ZIO.runtime[Layers.AppEnv with Clock].flatMap { implicit CE =>
      ZIO.effect(
        BlazeServerBuilder[Persistence.UserPersistenceRIO]
          .bindHttp(config.http.port, config.http.uri)
          .withHttpApp(httpApp)
      )
    }
  } yield blaze
}
