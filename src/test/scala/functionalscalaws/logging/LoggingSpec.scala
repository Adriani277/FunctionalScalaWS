package functionalscalaws.logging

import zio.test.DefaultRunnableSpec

import zio.test._
import zio.test.Assertion._
import zio.test.mock.Expectation._
import functionalscalaws.logging._
import zio.test.mock.MockConsole

object LoggingSpec extends DefaultRunnableSpec {
  def spec = suite("Console logger")(
    suite("info") {
      testM("prints to the console") {
        checkM(Gen.alphaNumericString) { str =>
          val env = MockConsole.PutStrLn(equalTo(s"info - $str"), value(())) >>> consoleLogger
          assertM(info(str).provideLayer(env))(isUnit)
        }
      }
    },
    suite("error") {
      testM("prints to the console") {
        checkM(Gen.alphaNumericString) { str =>
          val env = MockConsole.PutStrLn(equalTo(s"error - $str"), value(())) >>> consoleLogger
          assertM(error(str).provideLayer(env))(isUnit)
        }
      }
    }
  )
}
