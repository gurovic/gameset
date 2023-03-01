class Match(private val solutions: Seq[Solution], private val game: Game) {
  private var matchFinishedObserver: MatchFinishedObserver = _

  def run(observer: MatchFinishedObserver): Unit = {
    matchFinishedObserver = observer
    InvokerPull().getInstance().addToPool(
      new InvokerRequest(
        prepareInvokers(),
        createReport,
        Some(setupInvokers)
      )
    )
  }

  def prepareInvokers(): Array[Invoker] = {
    val invokers: Seq[Invoker] = Array[Invoker](solutions.length + 1)
    invokers.length.
  }

  def setupInvokers(): Unit = {

  }

  def createReport(): Unit = {
    matchFinishedObserver.receive()
  }
}
