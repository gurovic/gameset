class InvokerRequest(invokersNum: Int,
                     invokers: Array[Invoker],
                     callback: () => Unit,
                     setup: Option[() => Unit]) {

  def this(invokers: Array[Invoker], callback: () => Unit, setup: Option[() => Unit]) = {
    this(invokers.size, invokers, callback, setup)
  }

}