package functionalscalaws.services.db

import javax.sql.DataSource

import cats.effect.Blocker
import doobie.h2._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import functionalscalaws.Config.DoobieConfig
import io.github.gaelrenoux.tranzactio.ConnectionSource
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.interop.catz._

object HTransactor {

  val h2ConnectionSource = {
    val trans = (for {
      ce <- ExecutionContexts.fixedThreadPool[Task](32) // our connect EC
      be <- Blocker[Task]                               // our blocking EC
      xa <- H2Transactor.newH2Transactor[Task](
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", // connect URL
        "user",                               // username
        "",                                   // password
        ce,                                   // await connection here
        be                                    // execute JDBC operations here
      )
    } yield xa).toManagedZIO.map(_.kernel.getConnection())

    ZLayer.fromManaged(trans) ++ Clock.any ++ Blocking.any >>> ConnectionSource.fromConnection
  }

  val buildTransactor = ZLayer
    .fromManaged[zio.blocking.Blocking with Has[DoobieConfig], Nothing, DataSource] {
      val deps = for {
        ec      <- ZIO.descriptor.map(_.executor.asEC)
        blocker <- zio.blocking.blocking { ZIO.descriptor.map(_.executor.asEC) }
        conf    <- ZIO.access[Has[DoobieConfig]](_.get)
      } yield (ec, Blocker.liftExecutionContext(blocker), conf)

      deps.toManaged_.flatMap {
        case (ec, blocker, config) =>
          HikariTransactor
            .newHikariTransactor[Task](
              config.driver,
              config.url,
              config.user,
              config.password,
              ec,
              blocker
            )
            .toManagedZIO
            .map(_.kernel.getDataSource())
      }.orDie
    }
}
