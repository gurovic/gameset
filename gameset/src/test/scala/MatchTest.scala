import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import java.util

class MatchTest extends AnyFunSuite with BeforeAndAfter {

  var observer: MatchFinishedObserver = _
  var game: Game = _
  var testSolutionLists: Array[List[util.AbstractMap.SimpleEntry[Int, Solution]]] = _

  before {
    testSolutionLists = Array(
      List((0, new Solution())),
      List((0, new Solution()), (1, new Solution()), (2, new Solution())),
      List((1000000, new Solution()))
    )
  }

  test("match constructor") {
    for (i <- 0 to 2) {
      val match_ : Match = new Match(testSolutionLists(i), game)
    }
  }

  test("run") {
    for (i <- 0 to 2) {
      val match_ : Match = new Match(testSolutionLists(i), game)
      match_.run(observer)
    }
  }
}
