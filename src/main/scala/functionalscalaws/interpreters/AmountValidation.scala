package functionalscalaws.interpreters

import functionalscalaws.algebra.AmountValidationAlg
import functionalscalaws.domain._
import zio._

object AmountValidation {
  private val amountValidation = new AmountValidationAlg {
    def validate(amount: Amount): IO[InvalidAmountError, Unit] =
      if (amount.value <= 0 || amount.value > 1000000)
        IO.fail(InvalidAmountError(amount))
      else
        IO.unit
  }

  val live: ZLayer[Any, Nothing, Has[AmountValidationAlg]] = ZLayer.succeed(amountValidation)
}
