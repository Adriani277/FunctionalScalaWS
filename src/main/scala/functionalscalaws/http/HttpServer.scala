package functionalscalaws.http

import zhttp.service.Server

// import functionalscalaws.Config
import functionalscalaws.http.HelloRoutes
import zhttp.service.{EventLoopGroup}
import zio.{Scope, ZIO}
import zhttp.service.server
import functionalscalaws.UserProgram
import functionalscalaws.persistence._
// import org.http4s.implicits._
// import org.http4s.server.Router
// import org.http4s.server.blaze.BlazeServerBuilder
// import zio.Task
// import zio.config._
// import zio.interop.catz._
// import zio.interop.catz.implicits._

object HttpServer {
  val make =
    (for {
      _ <- Server.make(
        Server(HelloRoutes(UserProgram(InMemoryUserPersistence(Vector(User(1, "test"))))).app)
          .withPort(8080)
      )
      _ <- ZIO.logInfo("Started server")
      _ <- ZIO.never
    } yield ())
      .provideSomeLayer(EventLoopGroup.auto() ++ server.ServerChannelFactory.auto ++ Scope.default)

  // (Server
  //   .make(Server(HelloRoutes.app).withPort(8080)) *> ZIO.never)
  //   .provideSomeLayer(EventLoopGroup.auto() ++ server.ServerChannelFactory.auto ++ Scope.default)
//   val make = for {
//     config <- getConfig[Config]
//     service = HelloRoutes.helloWorldService
//     blaze <- Task.concurrentEffect.flatMap { implicit CE =>
//       service.map { route =>
//         BlazeServerBuilder[Task]
//           .bindHttp(config.http.port, config.http.uri)
//           .withHttpApp(Router("/" -> route).orNotFound)
//           .resource
//           .toManagedZIO
//       }
//     }
//   } yield blaze
}
