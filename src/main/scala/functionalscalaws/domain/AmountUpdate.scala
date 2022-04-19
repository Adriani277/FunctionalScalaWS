package functionalscalaws.domain

import java.util.UUID

final case class AmountUpdate private (id: UUID, amount: Amount)