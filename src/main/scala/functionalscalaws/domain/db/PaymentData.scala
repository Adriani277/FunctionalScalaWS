package functionalscalaws.domain.db

import java.util.UUID

import functionalscalaws.domain._

final case class PaymentData(id: UUID, name: Name, amount: Amount, recipient: Recipient)
