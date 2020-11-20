package functionalscalaws.http

import functionalscalaws.Config
import functionalscalaws.http.HelloRoutes
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.blaze._
import zio.Task
import zio.config._
import zio.interop.catz._
import zio.interop.catz.implicits._

object HttpServer {
  val make = for {
    config <- getConfig[Config]
    service = HelloRoutes.helloWorldService
    blaze <- Task.concurrentEffect.flatMap { implicit CE =>
      service.map { route =>
        BlazeServerBuilder[Task]
          .bindHttp(config.http.port, config.http.uri)
          .withHttpApp(Router("/" -> route).orNotFound)
          .resource
          .toManagedZIO
      }
    }
  } yield blaze
}
