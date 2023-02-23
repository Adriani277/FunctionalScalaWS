package functionalscalaws.program
import functionalscalaws.services.db.PaymentRepository
import zio._

trait PreStartupProgramAlg {
  def createTable: UIO[Unit]
}

final case class PreStartupProgram(database: PaymentRepository) extends PreStartupProgramAlg {
  def createTable: UIO[Unit] = database.createTable
}

object PreStartupProgram {
  val live: ZIO[PaymentRepository, Nothing, PreStartupProgramAlg] =
    ZIO.service[PaymentRepository].map(PreStartupProgram(_))
}
