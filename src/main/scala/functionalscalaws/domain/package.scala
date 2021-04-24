package functionalscalaws

package object domain {
  final case class Name private (value: String)
  final case class Recipient private (value: String)
  final case class Amount private (value: BigDecimal)
  object Amount {
    def apply(value: Double): Amount = Amount(BigDecimal(value))
  }
}
