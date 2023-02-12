package functionalscalaws.domain

sealed trait ServiceError                           extends Product with Serializable
final case class InvalidAmountError(amount: Amount) extends ServiceError
case object InvalidTransactionError                 extends ServiceError
