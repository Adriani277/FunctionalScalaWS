// package functionalscalaws.services.db
// import functionalscalaws.domain._
// import functionalscalaws.domain.db._
// import io.github.gaelrenoux.tranzactio.doobie._
// import zio._

// trait RepositoryAlg[A] {
//   def create(payment: Payment): UIO[A]
//   def update(amount: AmountUpdate): UIO[A]
//   def selectAll: UIO[List[A]]
//   def createTable: UIO[Unit]
// }
// object RepositoryAlg {
//   type PaymentRepository = RepositoryAlg[PaymentData]

//   val paymentRepoLive: ZLayer[Database, Nothing, Has[PaymentRepository]] =
//     (for {
//       db <- ZIO.service[Database.Service]
//     } yield new PaymentRepo(db)).toLayer

//   def create(payment: Payment): URIO[Has[PaymentRepository], PaymentData] =
//     ZIO.accessM(_.get.create(payment))

//   def update(amountUpdate: AmountUpdate): URIO[Has[PaymentRepository], PaymentData] =
//     ZIO.accessM(_.get.update(amountUpdate))

//   val selectAll: URIO[Has[PaymentRepository], List[PaymentData]] =
//     ZIO.accessM(_.get.selectAll)

//   val createTable: URIO[Has[PaymentRepository], Unit] =
//     ZIO.accessM(_.get.createTable)
// }


