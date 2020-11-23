package functionalscalaws.services

import zio.test.DefaultRunnableSpec

import zio.test._
import zio.test.Assertion._
import functionalscalaws.domain._

object TransactionValidationSpec extends DefaultRunnableSpec {
  def spec =
    suite("validate")(
      testM("returns InvalidTransactionError when name and recipient are equal") {
        val gen = Gen.alphaNumericStringBounded(1, 2).map(s => (Name(s), Recipient(s)))
        checkM(gen) {
          case (name, recipient) =>
            assertM(TransactionValidation.validate(name, recipient).run)(
              fails(equalTo(InvalidTransactionError))
            )
        }
      },
      testM("returns unit when name and recipient are not equal") {
        val gen = for {
          name      <- Gen.alphaNumericStringBounded(1, 10)
          recipient <- Gen.alphaNumericStringBounded(1, 10).filter(_ != name)
        } yield (Name(name), Recipient(recipient))
        checkM(gen) {
          case (name, recipient) =>
            assertM(TransactionValidation.validate(name, recipient))(isUnit)
        }
      }
    ).provideCustomLayer(TransactionValidation.Service.live)
}
