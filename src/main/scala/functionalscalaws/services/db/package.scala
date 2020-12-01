package functionalscalaws.services

import java.util.UUID

import doobie._
import doobie.implicits._
import functionalscalaws.domain._
import functionalscalaws.domain.db._
import io.github.gaelrenoux.tranzactio.doobie._
import zio._

package object db {
  type PaymentRepository = Has[Service[PaymentData]]

  trait Service[A] {
    def create(payment: Payment): UIO[A]
    def update(amount: AmountUpdate): UIO[A]
  }

  object Service {
    val paymentDataLive: ZLayer[Database, Nothing, PaymentRepository] =
      ZLayer.fromFunction(
        db =>
          new Service[PaymentData] {
            def create(payment: Payment): UIO[PaymentData] =
              Database
                .transactionOrDie(for {
                  id     <- UIO(UUID.randomUUID())
                  _      <- Repo.insert(id, payment)
                  result <- Repo.select(id)
                } yield result)
                .provide(db)
                .orDie

            def update(amountUpdate: AmountUpdate): UIO[PaymentData] =
              Database
                .transactionOrDie(for {
                  _      <- Repo.updateAmount(amountUpdate.id, amountUpdate.amount)
                  result <- Repo.select(amountUpdate.id)

                } yield result)
                .provide(db)
                .orDie
          }
      )
  }

  object DB {
    def create(payment: Payment): URIO[PaymentRepository, PaymentData] =
      ZIO.accessM(_.get.create(payment))

    def update(amountUpdate: AmountUpdate): URIO[PaymentRepository, PaymentData] =
      ZIO.accessM(_.get.update(amountUpdate))
  }
}

object Repo {

  /**
    * Needed in order to read/write a UUID to the database
    * This will convert the UUID into a string when persisting
    * and convert a String into a UUID when selecting
    */
  implicit val uuidMeta: Meta[UUID] = Meta[String].timap(UUID.fromString)(_.toString)

  def insert(id: UUID, payment: Payment) = tzio {
    sql"insert payments (id, name, amount, recipient) values (${id.toString}, ${payment.name.value}, ${payment.amount.value}, ${payment.recipient.value})".update.run
  }

  def updateAmount(id: UUID, amount: Amount) =
    tzio(sql"update payments set amount = ${amount.value} where id = ${id.toString()}".update.run)

  def select(id: UUID) =
    tzio(
      sql"select id, name, amount, recipient from payments where id = ${id.toString}"
        .query[PaymentData]
        .unique
    )
}
