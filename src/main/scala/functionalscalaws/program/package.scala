package functionalscalaws

import zio.Has

package object program {
  type PaymentCreationP = Has[PaymentCreationP.Service]
  type PaymentUpdateP   = Has[PaymentUpdateP.Service]
  type PreStartupP      = Has[PreStartupProgram.Service]
}
