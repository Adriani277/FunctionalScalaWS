package functionalscalaws.program

import functionalscalaws.domain.db.PaymentData
import functionalscalaws.services._
import functionalscalaws.services.db.Service
import zio._

trait PreStartupProgram {
  def createTable: UIO[Unit]
}

object PreStartupProgram {
  val live: ZLayer[Has[Service[PaymentData]],Nothing,Has[PreStartupProgram]] = (for {
    db <- ZIO.service[db.Service[PaymentData]]
  } yield new Program(db)).toLayer

  val createTable: URIO[Has[PreStartupProgram], Unit] =
    ZIO.accessM(_.get.createTable)

  final class Program(database: db.Service[PaymentData]) extends PreStartupProgram {
    def createTable: zio.UIO[Unit] = database.createTable
  }
}
