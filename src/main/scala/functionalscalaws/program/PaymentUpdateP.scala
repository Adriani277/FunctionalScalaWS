package functionalscalaws.program

import functionalscalaws.domain._
import functionalscalaws.domain.db._
import functionalscalaws.services.AmountValidation
import functionalscalaws.services.db.Repository
import zio._

object PaymentUpdateP {

  trait Service {
    def update(updateAmount: AmountUpdate): IO[ServiceError, PaymentData]
  }

  object Service {
    def live: ZLayer[Has[AmountValidation] with Has[Repository.PaymentRepository], Nothing, PaymentUpdateP] =
      ZLayer.fromFunction { services =>
        new Service {
          def update(updateAmount: AmountUpdate): zio.IO[ServiceError, PaymentData] =
            (for {
              _      <- AmountValidation.validate(updateAmount.amount)
              result <- Repository.update(updateAmount)
            } yield result).provide(services)
        }
      }
  }

  def update(
      updateAmount: AmountUpdate
  ): ZIO[PaymentUpdateP, ServiceError, PaymentData] =
    ZIO.accessM[PaymentUpdateP](_.get update updateAmount)
}
