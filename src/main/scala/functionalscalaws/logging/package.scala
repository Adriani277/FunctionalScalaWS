// package functionalscalaws

// import zio._
// import zio.clock.Clock
// import zio.console.`package`._
// import zio.logging._

// package object logging {
//   val consoleLogger: ZLayer[Console with Clock, Nothing, Logging] =
//     Logging.console(LogLevel.Info) >>> Logging.withRootLoggerName("app-logger")
// }
