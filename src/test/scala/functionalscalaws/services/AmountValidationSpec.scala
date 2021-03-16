package functionalscalaws.services

import functionalscalaws.domain._
import zio.test.Assertion._
import zio.test.DefaultRunnableSpec
import zio.test._

object AmountValidationSpec extends DefaultRunnableSpec {
  def spec =
    suite("validate")(
      testM("returns InvalidAmountError when amount is <= 0 or > 1,000,000") {
        val gen = {
          val v1 = Gen.double(Double.MinValue, 0d).map(Amount.apply)
          val v2 = Gen.double(1000000.0001d, Double.MaxValue).map(Amount.apply)
          Gen.oneOf(v1, v2)
        }
        checkM(gen) { amount =>
          assertM(AmountValidation.validate(amount).run)(
            fails(equalTo(InvalidAmountError(amount)))
          )
        }
      },
      testM("returns unit when amount is greater than 0 and < 1,000,000") {
        val gen = Gen.double(Double.MinPositiveValue, 1000000d).map(Amount.apply)
        checkM(gen) { amount =>
          assertM(AmountValidation.validate(amount))(
            isUnit
          )
        }
      }
    ).provideCustomLayer(AmountValidation.live)
}
