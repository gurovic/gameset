import  scala.collection.mutable.PriorityQueue
class InvokerPool(max_invokers: Int){

  private Int busy_invokers;
  private PriorityQueue[(Int, InvokerRequest)]()(Ordering.Int) request_queue;

  def waitForFreeSpace(): Unit = {

  }

  def addToQueue(invoker_request: InvokerRequest) Unit = {
    request_queue

  }

  def runInvokers(): Unit = {

  }

  def freeInvokerSpace(): Unit = {
    busy_invokers = busy_invokers - 1;
    waitForFreeSpace();
  }

}