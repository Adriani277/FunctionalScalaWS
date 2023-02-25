package functionalscalaws.domain.db

import java.util.UUID

import functionalscalaws.domain._

final case class PaymentData(id: UUID, name: String, amount: String, beneficiary: String)
