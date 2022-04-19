package functionalscalaws.algebra

import functionalscalaws.domain._
import zio._

trait AmountValidationAlg {
  def validate(amount: Amount): IO[InvalidAmountError, Unit]
}

object AmountValidationAlg {
//   val live: ZLayer[Any, Nothing, Has[AmountValidationAlg]] = // ZLayer.succeed(Interpreter)

  def validate(amount: Amount): ZIO[Has[AmountValidationAlg], InvalidAmountError, Unit] =
    ZIO.accessM(_.get.validate(amount))
}
