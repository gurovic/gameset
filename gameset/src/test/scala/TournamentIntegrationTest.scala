import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import java.lang.reflect.Field

class TournamentIntegrationTest extends AnyFunSuite with BeforeAndAfter {
  var tournament: Tournament = _
  val solutionLimit = 2
  before {
    tournament = new Tournament(new Game())
    tournament.solutionsLimit = solutionLimit
  }

  test("Tournament constructor") {
    assert(tournament.status == TournamentStatus.Pending)
    assert(tournament.solutionsLimit == solutionLimit)
  }

  test("Solution adding to pending tournament") {
    assertThrows[IllegalStateException] {
      tournament.addSolution(new Solution())
    }
  }

  test("Closing pending tournament") {
    assertThrows[IllegalStateException] {
      tournament.close()
    }
  }

  test("Opening tournament") {
    val start = System.currentTimeMillis()
    tournament.open()
    val finish = System.currentTimeMillis()
    assert(start <= tournament.openingTime.getTime && tournament.openingTime.getTime <= finish)
    assert(tournament.status == TournamentStatus.Opened)
  }

  test("Adding solutions to opened tournament") {
    tournament.open()
    val solution0 = new Solution()
    val solution1 = new Solution()
    tournament.addSolution(solution0)
    tournament.addSolution(solution1)

    val solutionsField: Field = tournament.getClass.getDeclaredField("solutions")
    solutionsField.setAccessible(true)
    val tourSolutions = solutionsField.get(tournament).asInstanceOf[List[Solution]]
    assert(tourSolutions(0) == solution0)
    assert(tourSolutions(1) == solution1)
  }

  test("Closing tournament (start testing)") {
    tournament.open()
    tournament.addSolution(new Solution())
    tournament.addSolution(new Solution())

    val start = System.currentTimeMillis()
    tournament.close()
    val finish = System.currentTimeMillis()
    assert(start <= tournament.closeTime.getTime && tournament.closeTime.getTime <= finish)
    assert(tournament.status == TournamentStatus.Testing)
  }

  test("Conclude tournament") {
    tournament.conclude(List())
  }
}
