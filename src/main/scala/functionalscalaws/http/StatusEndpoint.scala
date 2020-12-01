package functionalscalaws.http

import org.http4s.HttpRoutes
import org.http4s._
import org.http4s.dsl.Http4sDsl
import zio._
import zio.interop.catz._

final class StatusEndpoint[R] {
  type StatusTask[A] = RIO[R, A]

  private val dsl = Http4sDsl[StatusTask]
  import dsl._

  val routes = HttpRoutes.of[StatusTask] {
    case GET -> Root / "status" =>
      Ok("""{"status":"ok"}""")
  }
}
