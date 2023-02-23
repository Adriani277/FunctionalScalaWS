package functionalscalaws.domain

import java.util.UUID

final case class Payment private (id: UUID, name: Name, amount: Amount, recipient: Recipient)
