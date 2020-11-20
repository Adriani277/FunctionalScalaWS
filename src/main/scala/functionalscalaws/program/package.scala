package functionalscalaws

import zio.Has

package object program {
  type PaymentCreationP = Has[PaymentCreationP.Service]
}
