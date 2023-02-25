package functionalscalaws.domain

import java.util.UUID

final case class Payment(id: UUID, name: Name, amount: Amount, beneficiary: Beneficiary)
