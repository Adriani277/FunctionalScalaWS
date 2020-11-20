package functionalscalaws.http

import functionalscalaws.UserProgram
import functionalscalaws.persistence._
import org.http4s.HttpRoutes
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import zio.Task
import zio.URIO
import zio.interop.catz._

object HelloRoutes extends Http4sDsl[Task] {
  val helloWorldService = URIO.access[UserProgram.UserProgram with UserPersistence] { env =>
    HttpRoutes.of[Task] {
      case GET -> Root / "hello" / IntVar(id) =>
        implicit val encoder = jsonEncoderOf[Task, User]
        UserProgram.getUserWithLogging(id).provide(env).foldM(_ => NotFound(), Ok(_))

      case req @ POST -> Root / "hello" =>
        implicit val decoder = jsonOf[Task, User]
        implicit val encoder = jsonEncoderOf[Task, User]

        (for {
          user <- req.as[User]
          res  <- create[User](user)
        } yield res).foldM(_ => NotFound(), Ok(_)).provide(env)
    }
  }
}
