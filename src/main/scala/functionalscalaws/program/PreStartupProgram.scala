package functionalscalaws.program

import functionalscalaws.services._

import zio.UIO
import zio.ZLayer
import functionalscalaws.domain.db.PaymentData
import zio.URIO
import zio.ZIO

object PreStartupProgram {
  trait Service {
    def createTable: UIO[Unit]
  }

  object Service {
    val live = ZLayer.fromService[db.Service[PaymentData], Service](
      db =>
        new Service {
          def createTable: zio.UIO[Unit] = db.createTable
        }
    )
  }

  val createTable: URIO[PreStartupP, Unit] =
    ZIO.accessM(_.get.createTable)
}
