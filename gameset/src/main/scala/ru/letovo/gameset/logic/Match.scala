package ru.letovo.gameset.logic

import java.io.File
import java.nio.file.{Files, Paths}
import scala.sys.process.stringSeqToProcess

class Match(private val solutions: List[Solution], private val game: Game) {
  private var matchFinishedObserver: MatchFinishedObserver = _
  private var invokers: Array[Invoker] = _
  private val matchReport: MatchReport = new MatchReport
  private val pipePathRoot: String = Config.files_storage_root + "/pipes/";

  def run(observer: MatchFinishedObserver): Unit = {
    matchFinishedObserver = observer
    prepareInvokers()
    InvokerPool.addToQueue(
      new InvokerRequest(
        invokers,
        createReport,
        Some(setupInvokers)
      )
    )
  }

  private def prepareInvokers(): Unit = {
    val root = pipePathRoot + matchReport.matchId
    invokers = new Array[Invoker](solutions.length + 1)
    for (i <- solutions.indices) {
      invokers(i) = new Invoker(solutions(i).path, Seq())
      initInvokerInOutNames(invokers(i), root + "/" + i.toString);
    }

    prepareInteractor(root)
  }

  private def prepareInteractor(root: String): Unit = {
    val argv = List.tabulate(invokers.length - 1)(i =>
      invokers(i).stdin.get + ":" + invokers(i).stdout.get
    )
    val interactor = new Invoker(game.getInteractorPath(), argv)
    interactor.stdin = Option(root + "/interactor_in.txt")
    interactor.stdout = Option(root + "/interactor_out.txt")
    invokers(solutions.length) = interactor
  }

  private def setupInvokers(): Unit = {
    for (invoker <- invokers.dropRight(1)) {
      createPipe(invoker.stdin.get)
      createPipe(invoker.stdout.get)
    }
    new File(invokers.last.stdin.get).createNewFile()
    new File(invokers.last.stdout.get).createNewFile()
  }

  private def initInvokerInOutNames(invoker: Invoker, root: String): Unit = {
    invoker.stdin = Option(root + "_in" + "_inter" + ".pipe")
    invoker.stdout = Option(root + "_out" + "_inter" + ".pipe")
  }

  private def createPipe(path: String): Unit = {
    val parent = Paths.get(path).getParent
    if (parent != null) {
      Files.createDirectories(parent)
    }
    Seq("/usr/bin/env", "mkfifo", path).!
  }

  private def createReport(): Unit = {
    matchFinishedObserver.receive(new MatchReport())
  }
}
