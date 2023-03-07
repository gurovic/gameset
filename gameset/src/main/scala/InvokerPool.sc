import  scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Map
import import scala.util.Random
class InvokerPool(max_invokers: Int){

  private var busy_invokers = 0;



  private var request_queue = PriorityQueue[(Int, InvokerRequest]()(Ordering.Int);

  private var executed_invoker_requests = Map[String,Int];

  def waitForFreeSpace(): Unit = {
    var invokers_num = request_queue.head._2.invokersNum;
    if (max_invokers-busy_invokers >= invokers_num){
      runInvokers(request_queue.dequeue()._2)
    }
  }

  def addToQueue(invoker_request: InvokerRequest) Unit = {
    request_queue.enqueue((1, invoker_request));
    if (busy_invokers == 0){
      waitForFreeSpace();
    }

  }

  def runInvokers(invoker_request: InvokerRequest): Unit = {

    var invokers_num = InvokerRequest.invokerNum;
    var invokers_list = InvokerRequest.invokers;
    var request_id = Random.between(100000,999999).toString;

    executed_invoker_requests = executed_invoker_requests + ( request_id -> invokers_num);

    for (invoker <- invokers_list){
      invoker.run(freeInvokerSpace(request_id));
    }
  }

  def freeInvokerSpace(request_id: String): Unit = {
    var invokers_left = executed_invoker_requests.get(request_id);
    invokers_left -=1;
    if (invokers_left==0){
      //тут должна быть какая-то функция, которая что-то делает после того как все инвокеры в реквесте закончат работу
    }else {
      executed_invoker_requests = executed_invoker_requests + (request_id -> invokers_left)
    }
    busy_invokers += 1;
    waitForFreeSpace();
  }

}