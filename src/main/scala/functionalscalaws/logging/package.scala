package functionalscalaws

import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import zio._
import zio.console.`package`._
import zio.interop.catz._

package object logging {
  type Logging = Has[Service]

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
          override def info(s: String): zio.UIO[Unit]  = console.get.putStrLn(s"info - $s")
          override def error(s: String): zio.UIO[Unit] = console.get.putStrLn(s"error - $s")
        }
    )

  def info(s: String): ZIO[Logging, Nothing, Unit] =
    ZIO.accessM(_.get.info(s))

  def error(s: String): ZIO[Logging, Nothing, Unit] =
    ZIO.accessM(_.get.error(s))
}
