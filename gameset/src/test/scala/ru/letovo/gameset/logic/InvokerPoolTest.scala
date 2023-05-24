package ru.letovo.gameset.logic

import org.mockito.Mockito.when
import org.mockito.ArgumentMatchers.any
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito.{times, verify}
import scala.language.postfixOps

import scala.collection.immutable.Seq

class InvokerPoolTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar {

  var invoker: Invoker = mock[Invoker]

  var invoker_observer: InvokerObserver = mock[InvokerObserver]
  var invoker_request: InvokerRequest = _
  var callbackMock = mock[(InvokerReport) => Unit]


  before {



    val invokers = Array(invoker, invoker, invoker)

    def callback(): Unit = {}

    def setup(): Unit = {}

    invoker_request = new InvokerRequest(invokers, callback, Some(setup))
  }


  test("AddRequest") {
    InvokerPool.addToQueue(invoker_request)
    verify(invoker, times(3)).run(any[InvokerObserver], any[String])
    assert(InvokerPool.busy_invokers === 0)
  }


}
