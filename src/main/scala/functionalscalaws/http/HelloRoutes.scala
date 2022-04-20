package functionalscalaws.http

// import functionalscalaws.UserProgram
// import functionalscalaws.persistence._
// import org.http4s.HttpRoutes
// import org.http4s.circe._
// import org.http4s.dsl.Http4sDsl
// import zio.Task
// import zio.URIO
// import zio.interop.catz._

// object HelloRoutes extends Http4sDsl[Task] {
//   val helloWorldService = URIO.access[UserProgram.UserProgram with UserPersistence] { env =>
//     HttpRoutes.of[Task] {
//       case GET -> Root / "hello" / IntVar(id) =>
//         implicit val encoder = jsonEncoderOf[Task, User]
//         UserProgram.getUserWithLogging(id).provide(env).foldM(_ => NotFound(), Ok(_))

//       case req @ POST -> Root / "hello" =>
//         implicit val decoder = jsonOf[Task, User]
//         implicit val encoder = jsonEncoderOf[Task, User]

//         (for {
//           user <- req.as[User]
//           res  <- create[User](user)
//         } yield res).foldM(_ => NotFound(), Ok(_)).provide(env)
//     }
//   }
// }

import zhttp.http._
import functionalscalaws.UserProgramAlg
import zio._

final case class HelloRoutes(private val userProgram: UserProgramAlg) {
  val app = Http.collectZIO[Request] { case Method.GET -> !! / id =>
    for {
      id     <- ZIO.attempt(id.toInt)
      result <- userProgram.getUserWithLogging(id)
    } yield Response.text(result.fold("no user found")(_.toString))
  }
}
