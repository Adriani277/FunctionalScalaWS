package functionalscalaws.program

import functionalscalaws.domain._
import functionalscalaws.domain.db.PaymentData
import functionalscalaws.services._
import functionalscalaws.services.db.Repository._
import zio._

trait PaymentCreation {
  def create(payment: Payment): IO[ServiceError, PaymentData]
}

object PaymentCreation {
  val live: ZLayer[
    Has[AmountValidation] with Has[TransactionValidation] with Has[PaymentRepository],
    Nothing,
    Has[PaymentCreation]
  ] = (for {
    amountValidation      <- ZIO.service[AmountValidation]
    transactionValidation <- ZIO.service[TransactionValidation]
    repo                  <- ZIO.service[PaymentRepository]
  } yield new Program(amountValidation, transactionValidation, repo)).toLayer

  def create(payment: Payment): ZIO[Has[PaymentCreation], ServiceError, PaymentData] =
    ZIO.accessM(_.get.create(payment))

  final class Program(
      amountValidation: AmountValidation,
      transactionValidation: TransactionValidation,
      repo: PaymentRepository
  ) extends PaymentCreation {
    def create(payment: Payment): IO[ServiceError, PaymentData] =
      for {
        _         <- amountValidation.validate(payment.amount)
        _         <- transactionValidation.validate(payment.name, payment.recipient)
        persisted <- repo.create(payment)
      } yield persisted
  }
}
