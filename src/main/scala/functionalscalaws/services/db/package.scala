package functionalscalaws.services

import functionalscalaws.domain._
import functionalscalaws.domain.db._
import zio._
import java.util.UUID
import doobie._
import doobie.implicits._
import zio.interop.catz._

package object db {
  type PaymentRepository = Has[Service[PaymentData]]

  trait Service[A] {
    def create(payment: Payment): UIO[A]
    def update(amount: AmountUpdate): UIO[A]
  }

  object Service {
    val paymentDataLive: ZLayer[Has[Transactor[Task]], Nothing, PaymentRepository] =
      ZLayer.fromFunction(
        transactor =>
          new Service[PaymentData] {
            def create(payment: Payment): UIO[PaymentData] =
              (for {
                id     <- UIO(UUID.randomUUID())
                _      <- Database.insert(id, payment).run.transact(transactor.get).orDie
                result <- Database.select(id).unique.transact(transactor.get).orDie
              } yield result)

            def update(amountUpdate: AmountUpdate): UIO[PaymentData] =
              for {
                _ <- Database
                  .updateAmount(amountUpdate.id, amountUpdate.amount)
                  .run
                  .transact(transactor.get)
                  .orDie
                result <- Database.select(amountUpdate.id).unique.transact(transactor.get).orDie

              } yield result
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

object Database {

  /**
    * Needed in order to read/write a UUID to the database
    * This will convert the UUID into a string when persisting
    * and convert a String into a UUID when selecting
    */
  implicit val uuidMeta: Meta[UUID] = Meta[String].timap(UUID.fromString)(_.toString)

  def insert(id: UUID, payment: Payment) =
    sql"insert payments (id, name, amount, recipient) values (${id.toString}, ${payment.name.value}, ${payment.amount.value}, ${payment.recipient.value})".update

  def updateAmount(id: UUID, amount: Amount) =
    sql"update payments set amount = ${amount.value} where id = ${id.toString()}".update

  def select(id: UUID) =
    sql"select id, name, amount, recipient from payments where id = ${id.toString}"
      .query[PaymentData]
}
