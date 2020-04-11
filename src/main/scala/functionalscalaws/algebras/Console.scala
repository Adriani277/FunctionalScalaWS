package functionalscalaws.algebras

import cats.effect.Sync

trait Console[F[_]] {
  def putStrLn(s: String): F[Unit]
  def getStrLn(): F[String]
}

object Console {
  def make[F[_]: Sync] = new Console[F] {
    def getStrLn(): F[String]        = Sync[F].delay(scala.io.StdIn.readLine())
    def putStrLn(s: String): F[Unit] = Sync[F].delay(println(s))
  }
}
