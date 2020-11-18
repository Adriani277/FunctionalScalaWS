package functionalscalaws

import zio._
import zio.clock.Clock
import zio.console.`package`._
import zio.logging._

package object logging {
  // val logLogger = ZLayer.fromEffect(Slf4jLogger.create[Task].map { l =>
  //   new Service {
  //     def info(s: String): zio.UIO[Unit]  = l.info(s).orDie
  //     def error(s: String): zio.UIO[Unit] = l.error(s).orDie
  //   }
  // })

  val consoleLogger: ZLayer[Console with Clock, Nothing, Logging] =
    Logging.console(LogLevel.Info) >>> Logging.withRootLoggerName("app-logger")
}
