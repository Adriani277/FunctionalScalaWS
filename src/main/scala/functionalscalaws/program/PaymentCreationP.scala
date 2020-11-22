package functionalscalaws.program

import functionalscalaws.domain._
import functionalscalaws.domain.db.PaymentData
import zio._
import functionalscalaws.services._
import functionalscalaws.services.db._

object PaymentCreationP {
  trait Service {
    def create(payment: Payment): IO[ServiceError, PaymentData]
  }

  object Service {
    val live: ZLayer[
      AmountValidation with TransactionValidation with PaymentRepository,
      Nothing,
      PaymentCreationP
    ] = ZLayer
      .fromFunction { deps =>
        new Service {
          def create(payment: Payment): IO[ServiceError, PaymentData] =
            (for {
              _         <- AmountValidation.validate(payment.amount)
              _         <- TransactionValidation.validate(payment.name, payment.recipient)
              persisted <- DB.create(payment)
            } yield persisted).provide(deps)
        }
      }
  }

  def create(payment: Payment): ZIO[PaymentCreationP, ServiceError, PaymentData] =
    ZIO.accessM(_.get.create(payment))
}
