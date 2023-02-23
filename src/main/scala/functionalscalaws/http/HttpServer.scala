package functionalscalaws.http

import zio.http._
import zio.ZIO

object HttpServer {
  private val app = StatusEndpoint.route

  val server = Server.install(app).flatMap { port =>
    ZIO.logInfo(s"HTTP server started on port $port")
  } *> ZIO.never
}
