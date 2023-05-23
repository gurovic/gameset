package ru.letovo.gameset.logic

import models.Game
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doNothing
import org.scalatest.{BeforeAndAfter, PrivateMethodTester}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import ru.letovo.gameset.web.models.Solution

import java.util.function.Function
import scala.collection.mutable.ArrayBuffer
import java.lang.reflect.Field

class TournamentSystemRoundRobinTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar {
  private var tournamentSystemRoundRobin: TournamentSystemRoundRobin = _

  test("startTesting") {
    // Setup
    val solutions = List(mock[Solution], mock[Solution])
    val game = mock[Game]
    val callback = mock[Function[List[MatchReport], _]]
    val tournamentSystem = new TournamentSystemRoundRobin()

    // Exercise
    tournamentSystem.startTesting(solutions, game, callback)
    // Verify
    assert(tournamentSystem.matchesCompleted == tournamentSystem.matchesNumber)
    assert(tournamentSystem.matchReports.size == tournamentSystem.matchesNumber)
  }

  test("getNextMatch") {

  }

  test("generateSolutionGroups") {

  }

  test("generateMatchesNumber") {

  }

  test("getWinnerIndex") {

  }

  test("getLoserIndex") {

  }
}
