// package functionalscalaws.http

// import functionalscalaws.PersistenceMock
// import functionalscalaws.UserProgram
// import functionalscalaws.persistence._
// import org.http4s.Uri
// import org.http4s._
// import org.http4s.implicits._
// import zio._
// import zio.interop.catz._
// import zio.logging.Logging
// import zio.test.Assertion._
// import zio.test._
// import zio.test.mock.Expectation._

// object AllSuites extends DefaultRunnableSpec {
//   def spec = suite("All tests")(helloSuite)

//   val helloSuite = suite("HelloRoutesZIO")(
//     testM("returns Ok response for /hello/<name> endpoint") {
//       checkM(zio.test.Gen.anyInt) {
//         s =>
//           val persistence: ULayer[UserPersistence] =
//             PersistenceMock.Get(equalTo(s), value(User(s, "Adriani")))

//           val uri = Uri.unsafeFromString(s"/hello/$s")
//           val responseZ = HelloRoutes.helloWorldService.map(
//             _.orNotFound
//               .run(
//                 Request(method = Method.GET, uri = uri)
//               )
//           )

//           val test = for {
//             response <- responseZ
//             resp     <- response
//             body     <- resp.as[String]
//           } yield (resp.status, body)

//           val dep = Logging.ignore ++ persistence >+> UserProgram.Service.live

//           val result = test.provideLayer(dep)

//           assertM(result) {
//             Assertion.equalTo((Status.Ok, s"""{"id":$s,"v":"Adriani"}"""))
//           }
//       }
//     }
//   )
// }
