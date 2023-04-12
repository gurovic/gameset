package ru.letovo.gameset.logic

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class InvokerTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar {
  var invoker: Invoker = _
  var callbackMock = mock[(InvokerReport) => Unit]
  var path = System.getProperty("user.dir") + "\\data\\test\\hello_world"

  before {
    invoker = new Invoker(path, Seq())
    invoker.wallTimeLimitMs = 200
    callbackMock = mock[(InvokerReport) => Unit]
  }

  test("Invoker constructor") {
    assert(invoker.state === InvokerCreated())
  }

  test("Invoker.run") {
    invoker.run(callbackMock)
    Thread.sleep(300)
    assert(invoker.state.isInstanceOf[InvokerFinished])
  }

  test("Invoker callback") {
    invoker.run(callbackMock)
    Thread.sleep(300)
    verify(callbackMock, times(1)).apply(any[InvokerReport])
  }

  test("Time limit") {
    path = System.getProperty("user.dir") + "\\data\\test\\time_limit"
    invoker = new Invoker(path, Seq())
    invoker.wallTimeLimitMs = 200
    invoker.run(callbackMock)
    Thread.sleep(300)
    assert(invoker.state.isInstanceOf[InvokerFinished])
  }
}
