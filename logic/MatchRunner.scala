import java.io.{BufferedReader, File, FileReader}
import scala.collection.mutable.ArrayBuffer

//TODO: решить, какое количество решений у нас есть (сейчас в MatchReport есть поля Solution1 и Solution2, хотя вообще их может быть больше)

class MatchRunner(solutions: List[SolutionsRepository], interactorPath: String, matchCompletedObserver: MatchCompletedObserver) {
  def run(): Unit = {
    val matchID = 0 //TODO: add to constructor parameters

    val params = new ArrayBuffer[String]()
    for (elem <- solutions) {
      val inPipe = createPipe(createPipeName(elem, "in", matchID))
      val outPipe = createPipe(createPipeName(elem, "out", matchID))
      new Invoker(elem.getPath(), inPipe, outPipe, new Array[String](0)).run(false)

      params += inPipe + ":" + outPipe + ","
    }

    val interactorOut = new File(matchID + "_" + "interactor.out")
    interactorOut.createNewFile()
    val interactorIn = new File(matchID + "_" + "interactor.in")
    interactorIn.createNewFile()


    val startTime = System.currentTimeMillis().toInt
    new Invoker(interactorPath,
      interactorIn.getPath,
      interactorOut.getPath,
      params.toArray)
      .run(true)

    val reader = new BufferedReader(new FileReader(interactorOut))
    val finishTime = System.currentTimeMillis().toInt

    matchCompletedObserver.receiveMatchCompleted(
      MatchReport(
        matchID,
        finishTime - startTime,
        startTime,
        interactorOut.getPath,
        solutions(0).id,
        solutions(1).id,
        Integer.parseInt(reader.readLine())
      )
    )
  }

  private def createPipe(name: String): String = {
    val file = new File(name)
    file.createNewFile()
    file.getPath
  }

  private def createPipeName(solution: SolutionsRepository, pipeType: String, matchID: Int): String = {
    matchID + "_sol_" + solution.id + "_" + pipeType + ".pipe"
  }
}

trait MatchCompletedObserver {
  def receiveMatchCompleted(matchReport: MatchReport): Unit
}
