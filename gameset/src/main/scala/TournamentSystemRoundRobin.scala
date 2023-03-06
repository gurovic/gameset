import java.util
import java.util.function.Function


object Pair {
  // Return a map entry (key-value pair) from the specified values
  def of[T, U](first: T, second: U) = new util.AbstractMap.SimpleEntry[T, U](first, second)
}

class TournamentSystemRoundRobin extends TournamentSystem with MatchFinishedObserver {
  private var game: Game = _
  private var matchesCompleted = 0
  private var matchesNumber = 0
  private var solutions: util.List[Solution] = _
  private var matchReportsMatrix: util.List[util.List[MatchReport]] = _
  private var solutionGroups: util.List[util.List[util.Map.Entry[Solution, Integer]]] = _

  def startTesting(solutions: util.List[Solution], game: Game, callback: Function[util.List[util.List[MatchReport]], _]): Unit = {
    this.game = game
    this.solutions = solutions
    this.solutionGroups = generateSolutionGroups
    this.matchReportsMatrix = generateMatchReportsMatrix
    this.matchesNumber = generateMatchesNumber

    for (match_ <- getNextMatch) {
      match_.run(this)
    }

    while (this.matchesCompleted < this.matchesNumber) {
      Thread.sleep(1000)
    }

    callback(this.matchReportsMatrix)
  }

  private def getNextMatch: IndexedSeq[Match] = {
    for {
      i <- 0 until this.solutionGroups.size
      j <- 0 until this.solutionGroups[0].size
      if i != j
      if this.solutionGroups.get(i).get(j).getValue == 0
    } yield {
      val solution1 = this.solutionGroups.get(i).get(j).getKey
      val solution2 = this.solutionGroups.get(j).get(i).getKey
      new Match(util.List.of(Pair.of(i, solution1), Pair.of(j, solution2)), this.game)
    }
  }

  private def generateSolutionGroups: util.List[util.List[util.Map.Entry[Solution, Integer]]] = {
    val rows = this.solutions.size / 2
    val columns = this.solutions.size - rows
    val solutionGroups = new util.ArrayList[util.List[util.Map.Entry[Solution, Integer]]]
    var solutionIndex = 0

    for (i <- 0 until rows) {
      val row = new util.ArrayList[util.Map.Entry[Solution, Integer]]
      for (j <- 0 until columns) {
        row.add(Pair.of(this.solutions.get(solutionIndex), 0))
        solutionIndex += 1
      }
      solutionGroups.add(row)
    }

    solutionGroups
  }

  private def generateMatchReportsMatrix: util.List[util.List[MatchReport]] = {
    val rows = this.solutions.size / 2
    val columns = this.solutions.size - rows
    val matchReportsMatrix = new util.ArrayList[util.List[MatchReport]]

    for (_ <- 0 until rows) {
      val row = new util.ArrayList[MatchReport]

      for (_ <- 0 until columns) {
        row.add(null)
      }

      matchReportsMatrix.add(row)
    }

    matchReportsMatrix
  }

  private def generateMatchesNumber: Int = {
    this.solutions.size * (this.solutions.size - 1) / 2
  }

  override def receive(matchReport: MatchReport): Unit = {
    if (!matchReport.isDraw) {
      this.solutionGroups.get(matchReport.getWinnerIndex).get(matchReport.getLoserIndex).setValue(3)
      this.solutionGroups.get(matchReport.getLoserIndex).get(matchReport.getWinnerIndex).setValue(1)
    }
    else {
      this.solutionGroups.get(matchReport.getWinnerIndex).get(matchReport.getLoserIndex).setValue(2)
      this.solutionGroups.get(matchReport.getLoserIndex).get(matchReport.getWinnerIndex).setValue(2)
    }

    this.matchReportsMatrix.get(matchReport.getWinnerIndex).get(matchReport.getLoserIndex) = matchReport
    this.matchReportsMatrix.get(matchReport.getLoserIndex).get(matchReport.getWinnerIndex) = matchReport
    this.matchesCompleted += 1
  }
}
