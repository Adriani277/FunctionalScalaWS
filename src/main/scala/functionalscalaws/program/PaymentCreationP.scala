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
          println("Created Service")
          def create(payment: Payment): IO[ServiceError, PaymentData] = {
            val validated = for {
              _ <- AmountValidation.validate(payment.amount)
              _ <- TransactionValidation.validate(payment.name, payment.recipient)
            } yield payment

            println("Validation passed")
            validated.flatMap(DB.create)
          }.provide(deps)
        }
      }
  }

  def create(payment: Payment): ZIO[PaymentCreationP, ServiceError, PaymentData] =
    ZIO.accessM(_.get.create(payment))
}
