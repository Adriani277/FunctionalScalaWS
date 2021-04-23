package functionalscalaws.http

import zhttp.service.Server

object HttpServer {
  private val app = Server.port(8080) ++ Server.app(
    StatusEndpoint.route <> PaymentsEndpoint.routes
  )

  val server = Server.make(app)
}
