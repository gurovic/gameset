import java.util.Date
import scala.collection.mutable.ArrayBuffer


class Tournament(private val game : Game) {
  private var name: String = _
  var status = TournamentStatus.Pending
  private var tournamentSystem: TournamentSystem = _
  private var solutions: List[Solution] = _
  var openingTime: Date = _
  var closeTime: Date = _
  var solutionsLimit: Int = _

  def open(): Unit = {
    if (status != TournamentStatus.Pending) {
      throw new IllegalStateException("Tournament is not pending")
    }

    status = TournamentStatus.Opened
  }

  def close(): Unit = {
    if (status != TournamentStatus.Opened) {
      throw new IllegalStateException("Only opened tournament can be closed")
    }
    status = TournamentStatus.Testing
    tournamentSystem.startTesting(solutions, game, conclude)
  }

  def conclude(matchReports: List[MatchReport]): Unit = {
    // TODO
  }

  def addSolution(solution: Solution): Boolean = {
    if (status != TournamentStatus.Opened) {
      throw new IllegalStateException("Tournament is not opened for submitting solutions")
    } else if (solutions.size >= solutionsLimit) {
      throw new RuntimeException("Solution limit exceeded")
    }

    solutions = ArrayBuffer.from(solutions).addOne(solution).toList
    return true
  }
}
