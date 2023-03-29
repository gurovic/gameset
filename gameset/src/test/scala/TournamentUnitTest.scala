import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doNothing
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

import java.lang.reflect.Field

class TournamentUnitTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar {
  private var tournament: Tournament = _
  private val solutionLimit = 2

  before {
    val tournamentSystem = mock[TournamentSystem]
    tournament = new Tournament(
      "Test tour",
      new Game(),
      tournamentSystem,
      solutionLimit
    )
    tournament.solutionsLimit = solutionLimit

    doNothing().
    when(tournamentSystem).startTesting(
      any[List[Solution]],
      any[Game],
      any[java.util.function.Function[List[MatchReport], _]])
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
    val solutions = List(new Solution(), new Solution())
    for (sol <- solutions) {
      tournament.addSolution(sol)
    }

    val solutionsField: Field = tournament.getClass.getDeclaredField("solutions")
    solutionsField.setAccessible(true)
    val tourSolutions = solutionsField.get(tournament).asInstanceOf[List[Solution]]
    assert(tourSolutions == solutions)
  }

  test("Closing tournament (start testing)") {
    tournament.open()
    val solutions = List(new Solution(), new Solution())
    solutions.foreach(_ => tournament.addSolution(_))

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
