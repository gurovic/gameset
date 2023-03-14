import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

import scala.language.postfixOps

class InvokerRequestWithMockTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar {

  private val invoker = mock[Invoker]
  when(invoker.state)  thenReturn InvokerCreated()
  var invoker_request: InvokerRequest = _

  before {
    val invokers = Array(invoker, invoker, invoker)

    def callback(): Unit = {}

    def setup(): Unit = {}

    invoker_request = new InvokerRequest(invokers, callback, Some(setup))
  }

  test("InvokerRequest constructor") {

  }

  test("getInvokersNum") {

    assert(invoker_request.getInvokersNum === 3)
  }


  test("getInvokers") {

    assert(invoker_request.getInvokers.length === 3)

  }

  test("Invokers created with the right state") {

    assert(invoker_request.getInvokers()(1).state === InvokerCreated())
    def callback(invoker_report: InvokerReport): Unit = {

    }
    invoker_request.getInvokers()(0).run(callback)
  }
}