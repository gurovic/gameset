import org.mockito.Mockito.{times, verify}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class InvokerTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar {
  private val invoker_report = mock[InvokerReport]
  var invoker: Invoker = _
  val callbackMock = mock[(InvokerReport) => Unit]
  before {
    invoker = new Invoker("/", Seq())
  }

  test("Invoker constructor") {
    assert(invoker.state === InvokerCreated())
  }

  test("Check invoker running") {
    invoker.run(callbackMock)
    Thread.sleep(500)
    assert(invoker.state.isInstanceOf[InvokerFinished])
  }

  test("Check invoker callback") {
    verify(callbackMock, times(1)).apply(invoker_report)
  }
}