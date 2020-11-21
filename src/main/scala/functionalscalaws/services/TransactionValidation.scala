package functionalscalaws.services

import functionalscalaws.domain._
import zio._

object TransactionValidation {
  trait Service {
    def validate(sender: Name, recipient: Recipient): IO[InvalidTransactionError.type, Unit]
  }

  object Service {
    val live = ZLayer.fromEffect(UIO(new Service {
      def validate(sender: Name, recipient: Recipient): IO[InvalidTransactionError.type, Unit] =
        ZIO.when(sender.value == recipient.value)(ZIO.fail(InvalidTransactionError))
    }))
  }

  def validate(
      sender: Name,
      recipient: Recipient
  ): ZIO[TransactionValidation, InvalidTransactionError.type, Unit] =
    ZIO.accessM(_.get.validate(sender, recipient))
}
