package functionalscalaws

import zio.Has

package object program {
  type PaymentUpdateP   = Has[PaymentUpdateP.Service]
}
