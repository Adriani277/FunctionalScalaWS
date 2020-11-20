package functionalscalaws.domain

final case class Payment private (name: Name, amount: Amount, recipient: Recipient)
