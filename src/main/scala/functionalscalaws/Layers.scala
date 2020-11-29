package functionalscalaws

import functionalscalaws.UserProgram._
import functionalscalaws.configuration._
import functionalscalaws.logging._
import functionalscalaws.persistence._
import functionalscalaws.program._
import functionalscalaws.services._
import zio._
import zio.config.ZConfig
import zio.logging.Logging
import functionalscalaws.services.db.HTransactor

object Layers {
  type AppEnv = Logging
    with ZConfig[Config]
    with Persistence[User]
    with UserProgram
    with PaymentCreationP
    with PaymentUpdateP

  object live {
    private val programLayer = consoleLogger >+> inMemory(Vector.empty) >+> Service.live

    private val database = (doobieConfig ++ blocking.Blocking.live ++ clock.Clock.live) >+> HTransactor.buildTransactor >>> io.github.gaelrenoux.tranzactio.doobie.Database.fromDatasource
    private val repo       = database >>> db.Service.paymentDataLive
    private val paymentLayer =
      (repo ++ AmountValidation.Service.live ++ TransactionValidation.Service.live) >>> PaymentCreationP.Service.live ++ PaymentUpdateP.Service.live

    val appLayer: ZLayer[ZEnv, Throwable, AppEnv] = consoleLogger ++ programLayer ++ paymentLayer ++ liveConfig
  }
}
