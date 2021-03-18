package functionalscalaws.program

import functionalscalaws.algebra.AmountValidationAlg
import functionalscalaws.domain._
import functionalscalaws.domain.db.PaymentData
import functionalscalaws.services.db.RepositoryAlg
import functionalscalaws.services.db.RepositoryAlg.PaymentRepository
import zio.Has
import zio.ZLayer
import zio.test.Assertion._
import zio.test.DefaultRunnableSpec
import zio.test._
import zio.test.mock.Expectation._
import zio.test.mock.Mock

object PaymentUpdateProgramSpec extends DefaultRunnableSpec {
  def spec = suite("update")(
    testM("successfuly updates a payment") {
      val gen =
        for {
          id        <- Gen.anyUUID
          amount    <- Gen.bigDecimal(BigDecimal.exact(0), BigDecimal.exact("100000"))
          name      <- Gen.alphaNumericStringBounded(3, 10)
          recipient <- Gen.alphaNumericStringBounded(3, 10)
        } yield (
          AmountUpdate(id, Amount(amount)),
          PaymentData(id, Name(name), Amount(amount), Recipient(recipient))
        )

      checkM(gen) {
        case (am, persisted) =>
          val deps = AmountMock.Validate(equalTo(am.amount), value(())) andThen
            RepoMock
              .Update(equalTo(am), value(persisted))

          val updated =
            PaymentUpdateProgram.update(am).provideLayer(deps >>> PaymentUpdateProgram.live)
          assertM(updated)(equalTo(persisted))
      }
    }
  )
}

object AmountMock extends Mock[Has[AmountValidationAlg]] {
  object Validate extends Effect[Amount, InvalidAmountError, Unit]

  val compose: zio.URLayer[Has[zio.test.mock.Proxy], Has[AmountValidationAlg]] =
    ZLayer.fromService(
      invoke =>
        new AmountValidationAlg {
          def validate(amount: Amount): zio.IO[InvalidAmountError, Unit] =
            invoke(Validate, amount)
        }
    )
}

object RepoMock extends Mock[Has[PaymentRepository]] {

  object Create extends Effect[Payment, Nothing, PaymentData]
  object Update extends Effect[AmountUpdate, Nothing, PaymentData]

  val compose: zio.URLayer[Has[zio.test.mock.Proxy], Has[PaymentRepository]] = ZLayer.fromService(
    invoke =>
      new RepositoryAlg[PaymentData] {
        def createTable: zio.UIO[Unit]                         = ???
        def selectAll: zio.UIO[List[PaymentData]]              = ???
        def create(payment: Payment): zio.UIO[PaymentData]     = invoke(Create, payment)
        def update(amount: AmountUpdate): zio.UIO[PaymentData] = invoke(Update, amount)
      }
  )
}
