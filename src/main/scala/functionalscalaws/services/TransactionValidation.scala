package functionalscalaws.services

import functionalscalaws.domain._
import zio._

trait TransactionValidation {
  def validate(sender: Name, recipient: Recipient): IO[InvalidTransactionError.type, Unit]
}
object TransactionValidation {
  val live: ZLayer[Any, Nothing, Has[TransactionValidation]] = ZLayer.succeed(Interpreter)

  def validate(
      sender: Name,
      recipient: Recipient
  ): ZIO[Has[TransactionValidation], InvalidTransactionError.type, Unit] =
    ZIO.accessM(_.get.validate(sender, recipient))

  object Interpreter extends TransactionValidation {
    def validate(sender: Name, recipient: Recipient): IO[InvalidTransactionError.type, Unit] =
      ZIO.when(sender.value == recipient.value)(ZIO.fail(InvalidTransactionError))
  }
}
