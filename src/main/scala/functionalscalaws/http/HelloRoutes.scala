package functionalscalaws.http

import org.http4s.HttpRoutes, org.http4s._, org.http4s.dsl.Http4sDsl
import cats.{Defer, Applicative}

final class HelloRoutes[F[_]: Defer: Applicative] extends Http4sDsl[F] {
  val helloWorldService = HttpRoutes.of[F] {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name")
  }
}

object HelloRoutes {
  def apply[F[_]: Defer: Applicative]() = new HelloRoutes[F]
}
