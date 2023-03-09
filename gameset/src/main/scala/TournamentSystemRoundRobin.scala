import java.util.function.Function
import scala.collection.mutable.ArrayBuffer


object Pair {
  // Return a map entry (key-value pair) from the specified values
  def of[T, U](first: T, second: U) = new java.util.AbstractMap.SimpleEntry[T, U](first, second)
}

class TournamentSystemRoundRobin extends TournamentSystem with MatchFinishedObserver {
  private var game: Game = _
  private var matchesCompleted = 0
  private var matchesNumber = 0
  private var solutions: List[Solution] = _
  private var matchReports: ArrayBuffer[MatchReport] = _
  private var solutionGroups: List[List[java.util.Map.Entry[Solution, Integer]]] = _

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
      if this.solutionGroups(i)(j).getValue == 0
    } yield {
      val solution1 = this.solutionGroups(i)(j).getKey
      val solution2 = this.solutionGroups(j)(i).getKey
      new Match(List(Pair.of(i, solution1), Pair.of(j, solution2)), this.game)
    }
  }

  private def generateSolutionGroups: List[List[java.util.Map.Entry[Solution, Integer]]] = {
    val rows = this.solutions.size / 2
    val columns = this.solutions.size - rows
    val solutionGroups = ArrayBuffer.empty[List[java.util.Map.Entry[Solution, Integer]]]
    var solutionIndex = 0

    for (i <- 0 until rows) {
      val row = ArrayBuffer.empty[java.util.Map.Entry[Solution, Integer]]
      for (j <- 0 until columns) {
        row += (Pair.of(this.solutions(solutionIndex), 0))
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
      this.solutionGroups(getWinnerIndex(matchReport))(getLoserIndex(matchReport)).setValue(3)
      this.solutionGroups(getLoserIndex(matchReport))(getWinnerIndex(matchReport)).setValue(1)
    }
    else {
      this.solutionGroups(getWinnerIndex(matchReport))(getLoserIndex(matchReport)).setValue(2)
      this.solutionGroups(getLoserIndex(matchReport))(getWinnerIndex(matchReport)).setValue(2)
    }

    this.matchReports += (matchReport)
    this.matchesCompleted += 1
  }
}
