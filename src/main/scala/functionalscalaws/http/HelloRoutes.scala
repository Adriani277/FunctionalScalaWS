package functionalscalaws.http

import org.http4s.HttpRoutes, org.http4s._, org.http4s.dsl.Http4sDsl
import zio.interop.catz._
import functionalscalaws.algebras.Persistence._
import org.http4s.circe._

object HelloRoutes extends Http4sDsl[UserPersistenceRIO] {
  val helloWorldService = HttpRoutes.of[UserPersistenceRIO] {
    case GET -> Root / "hello" / IntVar(id) =>
      implicit val encoder = jsonEncoderOf[UserPersistenceRIO, User]
      get[User](id).foldM(_ => NotFound(), Ok(_))

    case req @ POST -> Root / "hello" =>
      implicit val decoder = jsonOf[UserPersistenceRIO, User]
      implicit val encoder = jsonEncoderOf[UserPersistenceRIO, User]

      (for {
        user <- req.as[User]
        res  <- create[User](user)
      } yield res).foldM(_ => NotFound(), Ok(_))
  }
}
