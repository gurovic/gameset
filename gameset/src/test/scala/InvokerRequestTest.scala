import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class InvokerRequestTest extends AnyFunSuite with BeforeAndAfter {

  var invoker_request: InvokerRequest = _

  before {
    val invokers = Array(new Invoker("3", Seq("3")), new Invoker("2", Seq("2")), new Invoker("1", Seq("1")))

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