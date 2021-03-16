package functionalscalaws.services

import functionalscalaws.domain._
import zio._

trait AmountValidation {
  def validate(amount: Amount): IO[InvalidAmountError, Unit]
}

object AmountValidation {
  val live: ZLayer[Any, Nothing, Has[AmountValidation]] = ZLayer.succeed(Interpreter)

  def validate(amount: Amount): ZIO[Has[AmountValidation], InvalidAmountError, Unit] =
    ZIO.accessM(_.get.validate(amount))

  object Interpreter extends AmountValidation {
    def validate(amount: Amount): IO[InvalidAmountError, Unit] =
      if (amount.value <= 0 || amount.value > 1000000)
        IO.fail(InvalidAmountError(amount))
      else
        IO.unit
  }
}
