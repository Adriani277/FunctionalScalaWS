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
}

final case class PaymentRepository(quill: Quill.H2[SnakeCase]) extends RepositoryAlg[PaymentData] {
  import quill._

  def create(payment: Payment): UIO[PaymentData] =
    (run {
      quote {
        query[PaymentData]
          .insert(
            _.id          -> lift(payment.id),
            _.name        -> lift(payment.name.value),
            _.beneficiary -> lift(payment.beneficiary.value),
            _.amount      -> lift(payment.amount.value.toString)
          )
          .returning(_.id)
      }
    }.orDie flatMap { id =>
      run {
        quote {
          query[PaymentData]
            .filter(a => a.id == lift(id))
            .distinct
        }
      }.head
    })
      .tapErrorCause { e => ZIO.logError(e.prettyPrint) }
      .orDieWith {
        case Some(e) => e
        case None    => new SQLException("No payment found")
      }

  def update(amount: AmountUpdate): UIO[PaymentData] = ???
  def selectAll: UIO[List[PaymentData]] =
    run {
      quote {
        query[PaymentData]
      }
    }.tapErrorCause { e => ZIO.logError(e.prettyPrint) }.orDie

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
