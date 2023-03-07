import java.util.Date
import java.util.List
import TournamentStatus._

class Tournament(private val game : Game) {
  private var name: String = _
  var status: TournamentStatus = Pending
  private var tournamentSystem: TournamentSystem = _
  private var solutions: List[Solution] = _
  var openingTime: Date = _
  var closeTime: Date = _
  var solutionsLimit: Int = _

  def open(): Unit = {
    if (status != Pending) {
      throw new IllegalStateException("Tournament is not pending")
    }

    status = Opened
  }

  def close(): Unit = {
    if (status != Opened) {
      throw new IllegalStateException("Only opened tournament can be closed")
    }
    status = Testing
    tournamentSystem.startTesting(solutions, game, conclude)
  }

  def conclude(matchReports: List[List[MatchReport]]): Unit = {
    // TODO
  }

  def addSolution(solution: Solution): Boolean = {
    if (status != Opened) {
      throw new IllegalStateException("Tournament is not opened for submitting solutions")
    } else if (solutions.length >= solutionsLimit) {
      throw new RuntimeException("Solution limit exceeded")
    }

    solutions = solutions :+ solution
    return true
  }
}
