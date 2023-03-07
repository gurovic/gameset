import java.util

class Match(private val solutions: IndexedSeq[util.AbstractMap.SimpleEntry[Int, Solution]], private val game: Game) {
  private var matchFinishedObserver: MatchFinishedObserver = _
  private var invokers: Array[Invoker] = _
  private val matchReport: MatchReport = new MatchReport


  def run(observer: MatchFinishedObserver): Unit = {
    matchFinishedObserver = observer
    prepareInvokers()
    InvokerPool().getInstance().addToPool(
      new InvokerRequest(
        invokers,
        createReport,
        Some(setupInvokers)
      )
    )
  }

  private def prepareInvokers(): Unit = {
    val matchID = matchReport.matchID
    val root = System.getProperty("user.dir") + matchID
    invokers = Array[Invoker](solutions.length + 1)
    for (i <- solutions.indices) {
      invokers(i) = new Invoker(solutions(i).getValue.path, Seq())
      invokers(i).stdin = Option(root + "in_" + i + ".pipe")
      invokers(i).stdout = Option(root + "out_" + i + ".pipe")
      invokers(i).stderr = Option(root + "err_" + i + ".pipe")
    }

    prepareInteractor(root)
  }

  private def prepareInteractor(root: String): Unit = {
    val argv = Seq[String](solutions.length)
    for (i <- 0 until invokers.length - 1) {
      argv(i) = invokers(i).stdin + ":" + invokers(i).stdout
    }

    val interactor = new Invoker(game.interactorPath, argv)
    interactor.stdin = Option(root + "in" + "_inter" + ".pipe")
    interactor.stdout = Option(root + "out" + "_inter" + ".pipe")
    interactor.stderr = Option(root + "err" + "_inter" + ".pipe")

    invokers(solutions.length) = interactor
  }

  private def setupInvokers(): Unit = {
    for (invoker <- invokers) {
      createPipe(invoker.stdin.get)
      createPipe(invoker.stdout.get)
      createPipe(invoker.stderr.get)
    }
  }

  private def createPipe(path: String): Unit = {
    val cmds = Array("/bin/sh", "-c", String.format("\"mkfifo ~/%s && tail -f ~/%s | csh -s\"", path, path))
    Runtime.getRuntime.exec(cmds)
  }

  private def createReport(): Unit = {
    matchFinishedObserver.receive(new MatchReport())
  }
}
