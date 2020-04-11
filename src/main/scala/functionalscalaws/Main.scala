package functionalscalaws

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import cats.implicits._
import cats.effect.Sync
import functionalscalaws.algebras.Console

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    program(Console.make[IO]) *> IO.pure(ExitCode.Success)
  }

  def program[F[_]: Sync](console: Console[F]): F[Unit] =
    for {
      _    <- console.putStrLn("Hello, What is your name?")
      name <- console.getStrLn()
      _    <- console.putStrLn(s"Nice to meet you $name")
    } yield ()
}
