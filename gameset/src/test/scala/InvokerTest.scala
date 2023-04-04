import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class InvokerTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar {
  var invoker: Invoker = _
  var callbackMock = mock[(InvokerReport) => Unit]

  before {
    invoker = new Invoker("/", Seq())
    invoker.wallTimeLimitMs = 200
    callbackMock = mock[(InvokerReport) => Unit]
  }

  test("Invoker constructor") {
    assert(invoker.state === InvokerCreated())
  }

  test("Check invoker running") {
    invoker.run(callbackMock)
    Thread.sleep(300)
    assert(invoker.state.isInstanceOf[InvokerFinished])
  }

  test("Check invoker callback") {
    invoker.run(callbackMock)
    Thread.sleep(300)
    verify(callbackMock, times(1)).apply(any[InvokerReport])
  }

}