package functionalscalaws

import zio.ZIO
import zio.UIO
import zio.Has
import zio.ZLayer
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import zio.console.`package`._
import zio.Task
import zio.interop.catz._

object Logging {
  type Logging = Has[Logging.Service]

  trait Service {
    def info(s: String): UIO[Unit]
    def error(s: String): UIO[Unit]
  }

  val logLogger = ZLayer.fromEffect(Slf4jLogger.create[Task].map { l =>
    new Service {
      def info(s: String): zio.UIO[Unit]  = l.info(s).orDie
      def error(s: String): zio.UIO[Unit] = l.error(s).orDie
    }
  })

  val consoleLogger: ZLayer[Console, Nothing, Logging] =
    ZLayer.fromFunction(
      console =>
        new Service {
          override def info(s: String): zio.UIO[Unit]  = console.get.putStr(s"info - $s")
          override def error(s: String): zio.UIO[Unit] = console.get.putStr(s"error - $s")
        }
    )

  def info(s: String): ZIO[Logging, Nothing, Unit] =
    ZIO.accessM(_.get.info(s))

  def error(s: String): ZIO[Logging, Nothing, Unit] =
    ZIO.accessM(_.get.error(s))
}
