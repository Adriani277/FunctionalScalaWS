package functionalscalaws.algebra

import functionalscalaws.domain._
import zio._

trait AmountValidationAlg {
  def validate(amount: Amount): IO[InvalidAmountError, Unit]
}

object AmountValidation {
  private val amountValidation = new AmountValidationAlg {
    def validate(amount: Amount): IO[InvalidAmountError, Unit] =
      if (amount.value <= 0 || amount.value > 1000000)
        ZIO.fail(InvalidAmountError(amount))
      else
        ZIO.unit
  }

  val live: ZLayer[Any, Nothing, AmountValidationAlg] = ZLayer.succeed(amountValidation)
}
