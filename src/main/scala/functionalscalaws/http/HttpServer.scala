package functionalscalaws.http

import zio.http._
import zio.ZIO

object HttpServer:
  val run =
    for {
      noteEndpoints  <- ZIO.service[NoteEndpoints]
      statusEndpoint <- ZIO.service[StatusEndpoint.type]
      allEndpoints = (noteEndpoints.routes ++ statusEndpoint.route).withDefaultErrorResponse
      _ <- Server
        .install(allEndpoints)
        .tap(port => ZIO.logInfo(s"HTTP server started on port $port"))
    } yield ()
