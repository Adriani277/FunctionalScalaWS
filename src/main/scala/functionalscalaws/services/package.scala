package functionalscalaws

import zio.Has

package object services {
  type AmountValidation      = Has[AmountValidation.Service]
  type TransactionValidation = Has[TransactionValidation.Service]
}
