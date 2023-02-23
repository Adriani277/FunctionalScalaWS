package functionalscalaws.http

import zio.http._
import zio.http.model.Method
import zio._
import zio.json._
import zio.json.ast.Json

object StatusEndpoint {
  val route = Http.collect { case Method.GET -> !! / "status" =>
    Response.json(Json.Str("OK").toJson)
  }
}
