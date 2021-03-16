package functionalscalaws

import functionalscalaws.configuration._
import functionalscalaws.logging._
import functionalscalaws.program._
import functionalscalaws.services._
import functionalscalaws.services.db.HTransactor
import functionalscalaws.services.db.Repository
import zio._
import zio.clock.Clock
import zio.config.ZConfig
import zio.logging.Logging
import zio.magic._

object Layers {
  type AppEnv = Logging
    with ZConfig[Config]
    // with Persistence[User]
    // with UserProgram
    with Has[PaymentCreation]
    with PaymentUpdateP
    with Clock
    with Has[PreStartupProgram]
    with Has[Repository.PaymentRepository]

  object live {
    //For database access comment lines 31/32 and uncomment 28/30
    val appLayer = ZLayer.fromSomeMagic[ZEnv, AppEnv](
      // io.github.gaelrenoux.tranzactio.doobie.Database.fromDatasource,
      // HTransactor.buildTransactor,
      // doobieConfig,
      io.github.gaelrenoux.tranzactio.doobie.Database.fromConnectionSource,
      HTransactor.h2ConnectionSource,
      Repository.paymentRepoLive,
      AmountValidation.live,
      TransactionValidation.live,
      PaymentCreation.live,
      PreStartupProgram.live,
      PaymentUpdateP.Service.live,
      liveConfig,
      consoleLogger
    )
  }
}
