package functionalscalaws.http

import zio.http._
import zio.ZIO

object HttpServer {
  val run = ZIO.service[PaymentsEndpoint].flatMap { paymentEndpoint =>
    val endpoints = paymentEndpoint.routes.withDefaultErrorResponse ++ StatusEndpoint.route

    Server.install(endpoints).flatMap { port =>
      ZIO.logInfo(s"HTTP server started on port $port")
    } *> ZIO.never
  }
}
