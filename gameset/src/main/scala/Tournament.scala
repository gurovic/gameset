import java.util.Date
import java.util
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.List
import scala.collection.JavaConverters._
import TournamentStatus._

class Tournament(private val game : Game, private val tournamentSystem : TournamentSystem) {
  private var name: String = _
  var status: TournamentStatus = Pending
  private var solutions: ListBuffer[Solution] = _
  var openingTime: Date = _
  var closeTime: Date = _
  var solutionsLimit: Int = _
  var results: List[MatchReport] = _

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
    tournamentSystem.startTesting(solutions.asJava, game, conclude)
  }

  private def conclude(matchReports: util.List[MatchReport]): Unit = {
    results = matchReports.asScala.toList
    status = Finished
  }

  def addSolution(solution: Solution): Boolean = {
    if (status != Opened) {
      throw new IllegalStateException("Tournament is not opened for submitting solutions")
    } else if (solutions.length >= solutionsLimit) {
      throw new RuntimeException("Solution limit exceeded")
    }

    solutions += solution
    return true
  }
}
