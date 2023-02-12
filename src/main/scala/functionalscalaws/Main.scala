package functionalscalaws

// import functionalscalaws.http.HttpServer
// import functionalscalaws.program.PreStartupProgram
import zio._

// import zio.logging._

final case class Example(name: String)

object Main extends ZIOAppDefault {
  

  override def run: ZIO[Any & (ZIOAppArgs & Scope), Any, Any] = 
  (for{
    config <- ZIO.service[MyConfig]
    _ <- Console.printLine(config.toString)
  } yield ()).provideLayer(MyConfig.live)

  // program.provideCustomLayer(Layers.live.appLayer).exitCode

  // private val program: ZIO[Layers.AppEnv, Throwable, Unit] =
  //   (for {
  //     _ <- log.info("Starting HTTP server")
  //     _ <- log.info("Creating DB")
  //     _ <- PreStartupProgram.createTable
  //   } yield ()) *> HttpServer.server.use { _ =>
  //     log.info("HTTP server started") *> ZIO.never
  //   }
}
