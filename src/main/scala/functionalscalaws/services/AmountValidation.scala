package functionalscalaws.services

import functionalscalaws.domain._
import zio.ZIO
import zio.IO

object AmountValidation {
  trait Service {
    def validate(amount: Amount): IO[InvalidAmountError, Unit]
  }

  object Service {
    val live: Service = new Service {
      def validate(amount: Amount): IO[InvalidAmountError, Unit] =
        if (amount.value <= 0 || amount.value > 1000000)
          IO.fail(InvalidAmountError(amount))
        else
          IO.unit
    }
  }

  def validate(amount: Amount): ZIO[AmountValidation, InvalidAmountError, Unit] =
    ZIO.accessM(_.get.validate(amount))
}
