import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import java.util

class MatchTest extends AnyFunSuite with BeforeAndAfter {

  var observer: MatchFinishedObserver = _
  var game: Game = new Game()
  var testSolutionLists: Array[List[util.AbstractMap.SimpleEntry[Int, Solution]]] = _

  before {
    testSolutionLists = Array(
      List(new util.AbstractMap.SimpleEntry(0, new Solution())),
      List(new util.AbstractMap.SimpleEntry(0, new Solution()), new util.AbstractMap.SimpleEntry(1, new Solution()),
        new util.AbstractMap.SimpleEntry(2, new Solution())),
      List(new util.AbstractMap.SimpleEntry(1000000, new Solution()))
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
