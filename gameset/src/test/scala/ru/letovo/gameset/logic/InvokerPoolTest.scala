package ru.letovo.gameset.logic

import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import scala.language.postfixOps

import scala.collection.immutable.Seq

class InvokerPoolTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar {

  var invoker: Invoker = _
  var invoker_request: InvokerRequest = _
  var callbackMock = mock[(InvokerReport) => Unit]
  var path = System.getProperty("user.dir") + "/data/test/hello_world"

  before {
    invoker = new Invoker(path, Seq())
    invoker.wallTimeLimitMs = 200
    callbackMock = mock[(InvokerReport) => Unit]
    val invokers = Array(invoker, invoker, invoker)

    def callback(): Unit = {}

    def setup(): Unit = {}

    invoker_request = new InvokerRequest(invokers, callback, Some(setup))
  }


  test("AddRequest") {

    assert(InvokerPool.addToQueue(invoker_request) === 3)
  }


}
