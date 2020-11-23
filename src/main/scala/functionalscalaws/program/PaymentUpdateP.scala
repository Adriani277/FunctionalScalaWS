package functionalscalaws.program

import functionalscalaws.domain._
import functionalscalaws.domain.db._
import zio._
import functionalscalaws.services.AmountValidation
import functionalscalaws.services.db._

object PaymentUpdateP {

  trait Service {
    def update(updateAmount: AmountUpdate): IO[ServiceError, PaymentData]
  }

  object Service {
    def live = ZLayer.fromFunction { (services: AmountValidation with PaymentRepository) =>
      new Service {
        def update(updateAmount: AmountUpdate): zio.IO[ServiceError, PaymentData] =
          (for {
            _      <- AmountValidation.validate(updateAmount.amount)
            result <- update(updateAmount)
          } yield result).provide(services)
      }
    }
  }

  def update(updateAmount: AmountUpdate) =
    ZIO.accessM[PaymentUpdateP](_.get update updateAmount)
}
