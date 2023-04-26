package ru.letovo.gameset.logic

import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Map
import scala.util.Random

object InvokerPool extends InvokerObserver  {


  private var busy_invokers = 0;

  private val max_invokers = 10;

  private var request_queue = PriorityQueue[(Int, InvokerRequest)]()(Ordering.by(_._1))


  private var executed_invoker_requests = Map[String,(Int, InvokerRequest)]();

  def waitForFreeSpace(): Unit = {
    var invokers_num = request_queue.head._2.getInvokersNum();
    if (max_invokers-busy_invokers >= invokers_num){
      runInvokers(request_queue.dequeue()._2)
    }
  }

  def addToQueue(invoker_request: InvokerRequest): Unit = {
    request_queue.enqueue((1, invoker_request));
    if (busy_invokers == 0){
      waitForFreeSpace();
    }

  }

  def runInvokers(invoker_request: InvokerRequest): Unit = {

    var invokers_num = invoker_request.getInvokersNum();
    var invokers_list = invoker_request.getInvokers();
    var request_id = Random.between(100000,999999).toString;

    executed_invoker_requests = executed_invoker_requests + ( request_id -> (invokers_num, invoker_request));

    for (invoker <- invokers_list){
      // TODO fix me!
      invoker.run( this, request_id);
    }
  }


  override def recieve(request_id: String): Unit = {
    var invokers_left = executed_invoker_requests.get(request_id).get._1;
    var invoker_request: InvokerRequest = executed_invoker_requests.get(request_id).get._2;
    invokers_left -= 1;
    if (invokers_left == 0) {
      invoker_request.callback()
    }
    busy_invokers += 1;
    waitForFreeSpace();

  }
}
