import sun.awt.shell.ShellFolder.Invoker

class Match(private val solutions: Seq[Solution], private val game: Game) {
  private var matchFinishedObserver: MatchFinishedObserver = _
  private var invokers: Array[Invoker] = _
  private val matchReport: MatchReport = _

  def run(observer: MatchFinishedObserver): Unit = {
    matchFinishedObserver = observer
    prepareInvokers()
    InvokerPull().getInstance().addToPool(
      new InvokerRequest(
        invokers,
        createReport,
        Some(setupInvokers)
      )
    )
  }

  def prepareInvokers(): Unit = {
    val matchID = matchReport.id
    val root = System.getProperty("user.dir") + matchID
    invokers = Array[Invoker](solutions.length + 1)
    for (i <- solutions.indices) {
      invokers[i] = new Invoker(solutions[i].path)
      invokers[i].redirectStdin = root + "in_" + i + ".pipe"
      invokers[i].redirectStdout = root + "out_" + i + ".pipe"
      invokers[i].redirectStderr = root + "err_" + i + ".pipe"
    }

    prepareInteractor(root)
  }

  def prepareInteractor(root: String): Unit = {
    val argv = new StringBuilder()
    for (i <- 0 until invokers.length - 1) {
      argv.append(invokers[i].redirectStdin).append(":").append(invokers[i].redirectStdout).append(",")
    }

    val interactor = new Invoker(game.interactorPath)
    interactor.argv = argv
    interactor.redirectStdin = root + "in" + "_inter" + ".pipe"
    interactor.redirectStdout = root + "out" + "_inter" + ".pipe"
    interactor.redirectStderr = root + "err" + "_inter" + ".pipe"

    invokers[solutions.length] = interactor
  }

  def setupInvokers(): Unit = {
    for (invoker <- invokers) {
      createPipe(invoker.redirectStdin)
      createPipe(invoker.redirectStdout)
      createPipe(invoker.redirectStderr)
    }
  }

  def createPipe(path: String): Unit = {
    val cmds = Array("/bin/sh", "-c", String.format("\"mkfifo ~/%s && tail -f ~/%s | csh -s\"", path, path))
    Runtime.getRuntime.exec(cmds)
  }

  def createReport(): Unit = {
    matchFinishedObserver.receive(new MatchReport())
  }
}
