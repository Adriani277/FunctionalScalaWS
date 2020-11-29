package functionalscalaws.services.db

import cats.effect.Blocker
import doobie.hikari.HikariTransactor
import zio._
import functionalscalaws.Config._
import zio.interop.catz._
import functionalscalaws.Config.DoobieConfig
import javax.sql.DataSource

object HTransactor {

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
