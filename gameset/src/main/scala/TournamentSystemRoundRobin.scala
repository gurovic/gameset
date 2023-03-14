import java.util.function.Function
import scala.collection.mutable.ArrayBuffer

class TournamentSystemRoundRobin extends TournamentSystem with MatchFinishedObserver {
  private var game: Game = _
  private var matchesCompleted = 0
  private var matchesNumber = 0
  private var solutions: List[Solution] = _
  private var matchReports: ArrayBuffer[MatchReport] = _
  private var solutionGroups: List[List[MutablePair[Solution, Integer]]] = _

  override def startTesting(solutions: List[Solution], game: Game, callback: Function[List[MatchReport], _]): Unit = {
    this.game = game
    this.solutions = solutions
    this.solutionGroups = generateSolutionGroups
    this.matchesNumber = generateMatchesNumber

    for (match_ <- getNextMatch) {
      match_.run(this)
    }

    while (this.matchesCompleted < this.matchesNumber) {
      Thread.sleep(1000)
    }

    callback(this.matchReports.toList)
  }

  private def getNextMatch: IndexedSeq[Match] = {
    for {
      i <- 0 until this.solutionGroups.size
      j <- 0 until this.solutionGroups(0).size
      if i != j
      if this.solutionGroups(i)(j).value == 0
    } yield {
      val solution1 = this.solutionGroups(i)(j).key
      val solution2 = this.solutionGroups(j)(i).key
      new Match(List(MutablePair(i, solution1), MutablePair(j, solution2)), this.game)
    }
  }

  private def generateSolutionGroups: List[List[MutablePair[Solution, Integer]]] = {
    val rows = this.solutions.size / 2
    val columns = this.solutions.size - rows
    val solutionGroups = ArrayBuffer.empty[List[MutablePair[Solution, Integer]]]
    var solutionIndex = 0

    for (i <- 0 until rows) {
      val row = ArrayBuffer.empty[MutablePair[Solution, Integer]]
      for (j <- 0 until columns) {
        row += MutablePair(this.solutions(solutionIndex), 0)
        solutionIndex += 1
      }
      solutionGroups += row.toList
    }

    solutionGroups.toList
  }

  private def generateMatchesNumber: Int = {
    this.solutions.size * (this.solutions.size - 1) / 2
  }

  private def getWinnerIndex(matchReport: MatchReport): Int = {
    if (matchReport.solutionScores(0) > matchReport.solutionScores(1)) 0 else 1
  }
  private def getLoserIndex(matchReport: MatchReport): Int = {
    if (matchReport.solutionScores(0) > matchReport.solutionScores(1)) 1 else 0
  }

  override def receive(matchReport: MatchReport): Unit = {
    if (matchReport.solutionScores(0) != matchReport.solutionScores(1)) {
      this.solutionGroups(getWinnerIndex(matchReport))(getLoserIndex(matchReport)).value = 3
      this.solutionGroups(getLoserIndex(matchReport))(getWinnerIndex(matchReport)).value = 1
    }
    else {
      this.solutionGroups(getWinnerIndex(matchReport))(getLoserIndex(matchReport)).value = 2
      this.solutionGroups(getLoserIndex(matchReport))(getWinnerIndex(matchReport)).value = 2
    }

    this.matchReports += (matchReport)
    this.matchesCompleted += 1
  }
}
