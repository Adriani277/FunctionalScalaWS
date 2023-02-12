// package functionalscalaws.program
// import functionalscalaws.services.db.RepositoryAlg.PaymentRepository
// import zio._

// trait PreStartupProgram {
//   def createTable: UIO[Unit]
// }

// object PreStartupProgram {
//   val live: ZLayer[Has[PaymentRepository],Nothing,Has[PreStartupProgram]] = (for {
//     db <- ZIO.service[PaymentRepository]
//   } yield new Program(db)).toLayer

//   val createTable: URIO[Has[PreStartupProgram], Unit] =
//     ZIO.accessM(_.get.createTable)

//   final class Program(database: PaymentRepository) extends PreStartupProgram {
//     def createTable: zio.UIO[Unit] = database.createTable
//   }
// }
