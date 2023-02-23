package functionalscalaws.services.db
import functionalscalaws.domain._
import functionalscalaws.domain.db._
// import io.github.gaelrenoux.tranzactio.doobie._
import zio._
import io.getquill.jdbczio.Quill
import io.getquill.SnakeCase
import io.getquill._
import java.sql.SQLException

trait RepositoryAlg[A] {
  def create(payment: Payment): UIO[A]
  def update(amount: AmountUpdate): UIO[A]
  def selectAll: UIO[List[A]]
  def createTable: UIO[Unit]
}

final case class PaymentRepository(quill: Quill.H2[SnakeCase]) {
  import quill._

  def createTable: UIO[Unit] =
    ZIO.logInfo("Creating table") *>
      run {
        quote {

          sql"""CREATE TABLE payments (
                id VARCHAR(36),
                name VARCHAR(255),
                amount DECIMAL(12, 2),
                recipient VARCHAR(255)
                )""".asCondition
        }
      }.orDie.ignore

  inline def create(payment: Payment): ZIO[Any, SQLException, PaymentData] =
    run {
      query[PaymentData]
        .insert(
          _.name      -> payment.name,
          _.recipient -> payment.recipient,
          _.amount    -> payment.amount
        )
        .returning(identity)
    }

  def update(amount: AmountUpdate): UIO[PaymentData] = ???
  // runIO {
  //   quote {
  //     query[PaymentData].filter(_.id == lift(amount.id)).update(_.amount -> lift(amount.amount))
  //   }
  // }.map(_ => PaymentData(amount.id, amount.amount))

  def selectAll: UIO[List[PaymentData]] = ???
  // runIO {
  //   quote {
  //     query[PaymentData]
  //   }
  // }

}

object PaymentRepository {
  val live = ZLayer.fromFunction(PaymentRepository(_))
}

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
