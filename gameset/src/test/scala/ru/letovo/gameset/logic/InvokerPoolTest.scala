package ru.letovo.gameset.logic

import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import scala.language.postfixOps

import scala.collection.immutable.Seq

class InvokerPoolTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar {

  private val invoker = mock[Invoker]
  when(invoker.state) thenReturn InvokerCreated()
  var invoker_request: InvokerRequest = _

  var invoker_pool: InvokerPool = _


  before {
    val invokers = Array(invoker, invoker, invoker)

    def callback(): Unit = {}

    def setup(): Unit = {}

    invoker_request = new InvokerRequest(invokers, callback, Some(setup))

    invoker_pool = new InvokerPool(20)
  }


  test("AddRequest") {

    assert(invoker_pool.addToQueue(invoker_request) === 3)
  }


}
