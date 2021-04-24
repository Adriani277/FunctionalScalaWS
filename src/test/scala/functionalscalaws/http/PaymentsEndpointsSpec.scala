package functionalscalaws.http

import functionalscalaws.domain.AmountUpdate
import functionalscalaws.domain.Payment
import functionalscalaws.domain.db.PaymentData
import functionalscalaws.program._
import functionalscalaws.services.db.RepositoryAlg
import org.http4s.Uri
import zhttp.http._
import zio._
import zio.test.DefaultRunnableSpec
import zio.test._
import zio.test.mock.Mock
import zio.test.mock.mockable

object PaymentsEndpointsSpec extends DefaultRunnableSpec {
  def spec: ZSpec[Environment, Failure] = suite("all tests")(
    suite("status")(
      testM("returns ok json") {
        Uri.unsafeFromString("/status")
        val request  = Request((Method.GET, URL(Path("/status"))))
        val response = PaymentsEndpoint.routes.evalAsEffect(request)

        assertM(response)(Assertion.equalTo(Response.jsonString("""{"status":"ok"}""")))
          .provideLayer(
            PaymentCreationMock.empty ++ PaymentUpdateProgramMock.empty ++ PaymentRepositoryMock.empty
          )
      }
    )
  )
}

@mockable[PaymentCreation]
object PaymentCreationMock

@mockable[PaymentUpdateProgram]
object PaymentUpdateProgramMock

object PaymentRepositoryMock extends Mock[Has[RepositoryAlg.PaymentRepository]] {
  object Create      extends Effect[Payment, Nothing, PaymentData]
  object Update      extends Effect[AmountUpdate, Nothing, PaymentData]
  object SelectAll   extends Effect[Unit, Nothing, List[PaymentData]]
  object CreateTable extends Effect[Unit, Nothing, Unit]

  val compose: URLayer[Has[mock.Proxy], Has[RepositoryAlg.PaymentRepository]] =
    ZLayer.fromService { proxy =>
      new RepositoryAlg.PaymentRepository {
        def create(payment: Payment): UIO[PaymentData]     = proxy(Create, payment)
        def update(amount: AmountUpdate): UIO[PaymentData] = proxy(Update, amount)
        def selectAll: UIO[List[PaymentData]]              = proxy(SelectAll)
        def createTable: UIO[Unit]                         = proxy(CreateTable)
      }
    }
}
