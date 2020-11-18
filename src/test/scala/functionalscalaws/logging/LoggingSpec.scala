package functionalscalaws.logging

import java.time.OffsetDateTime

import functionalscalaws.logging._
import zio.logging.log
import zio.test.Assertion._
import zio.test.DefaultRunnableSpec
import zio.test._
import zio.test.mock.Expectation._
import zio.test.mock.MockClock
import zio.test.mock.MockConsole

object LoggingSpec extends DefaultRunnableSpec {
  def spec = suite("Console logger")(
    suite("info") {
      testM("prints to the console") {
        checkM(Gen.alphaNumericString) { str =>
          val clock   = MockClock.CurrentDateTime(value(OffsetDateTime.MAX))
          val console = MockConsole.PutStrLn(containsString("info") && containsString(str), value(()))
          val env     = clock ++ console >>> consoleLogger
          assertM(log.info(str).provideLayer(env))(isUnit)
        }
      }
    },
    suite("error") {
      testM("prints to the console") {
        checkM(Gen.alphaNumericString) { str =>
          val clock   = MockClock.CurrentDateTime(value(OffsetDateTime.MAX))
          val console = MockConsole.PutStrLn(containsString("error") && containsString(str), value(()))
          val env     = clock ++ console >>> consoleLogger
          assertM(log.error(str).provideLayer(env))(isUnit)
        }
      }
    }
  )
}
