// package functionalscalaws.services.db

// import java.util.UUID

// import doobie._
// import doobie.implicits._
// import functionalscalaws.domain._
// import functionalscalaws.domain.db._
// import io.github.gaelrenoux.tranzactio.doobie._
// import zio._

// final class PaymentRepo(db: Database.Service) extends RepositoryAlg[PaymentData] {
//   def create(payment: Payment): UIO[PaymentData] =
//     db.transactionOrDie(for {
//         id     <- UIO(UUID.randomUUID())
//         _      <- PaymentRepo.insert(id, payment)
//         result <- PaymentRepo.selectById(id)
//       } yield result)
//       .orDie

//   def update(amountUpdate: AmountUpdate): UIO[PaymentData] =
//     db.transactionOrDie(for {
//         _      <- PaymentRepo.updateAmount(amountUpdate.id, amountUpdate.amount)
//         result <- PaymentRepo.selectById(amountUpdate.id)

//       } yield result)
//       .orDie

//   def selectAll: zio.UIO[List[PaymentData]] =
//     db.transactionOrDie(PaymentRepo.selectAll).orDie

//   def createTable: zio.UIO[Unit] =
//     db.transactionOrDie(PaymentRepo.createTable)
// }

// object PaymentRepo {

//   /**
//     * Needed in order to read/write a UUID to the database
//     * This will convert the UUID into a string when persisting
//     * and convert a String into a UUID when selecting
//     */
//   implicit val uuidMeta: Meta[UUID] = Meta[String].timap(UUID.fromString)(_.toString)

//   def insert(id: UUID, payment: Payment) = tzio {
//     sql"insert into payments (id, name, amount, recipient) values (${id.toString}, ${payment.name.value}, ${payment.amount.value}, ${payment.recipient.value})".update.run
//   }

//   def updateAmount(id: UUID, amount: Amount) =
//     tzio(sql"update payments set amount = ${amount.value} where id = ${id.toString()}".update.run)

//   def selectById(id: UUID) =
//     tzio(
//       sql"select id, name, amount, recipient from payments where id = ${id.toString}"
//         .query[PaymentData]
//         .unique
//     )

//   val selectAll =
//     tzio(
//       sql"select id, name, amount, recipient from payments"
//         .query[PaymentData]
//         .to[List]
//     )

//   val createTable = tzio {
//     sql"""CREATE TABLE payments (
//                 id VARCHAR(36),
//                 name VARCHAR(255),
//                 amount DECIMAL(12, 2),
//                 recipient VARCHAR(255)
//               ) """.update.run
//   }.orDie.unit
// }
