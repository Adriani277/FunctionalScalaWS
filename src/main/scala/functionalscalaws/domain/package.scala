package functionalscalaws

package object domain {
  final case class Name(value: String)
  final case class Beneficiary(value: String)
  final case class Amount(value: BigDecimal)
  object Amount {
    def apply(value: Double): Amount = Amount(BigDecimal(value))
  }
}
