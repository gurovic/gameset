package ru.letovo.gameset.logic

import models.Game
import ru.letovo.gameset.web.models.Solution

import java.util.{Calendar, Date}
import scala.collection.mutable.ArrayBuffer


class Tournament(
                  private var name: String,
                  private val game: Game,
                  private val tournamentSystem: TournamentSystem,
                  var solutionsLimit: Int,
                  private val observer: TournamentFinishedObserver
                ) {
  var status: TournamentStatus.Value = TournamentStatus.Pending
  private var solutions: List[Solution] = _
  var openingTime: Date = _
  var closeTime: Date = _

  def open(): Unit = {
    if (status != TournamentStatus.Pending) {
      throw new IllegalStateException("Tournament is not pending")
    }

    solutions = List()
    openingTime = Calendar.getInstance().getTime
    status = TournamentStatus.Opened
  }

  def close(): Unit = {
    if (status != TournamentStatus.Opened) {
      throw new IllegalStateException("Only opened tournament can be closed")
    }
    status = TournamentStatus.Testing
    closeTime = Calendar.getInstance().getTime
    tournamentSystem.startTesting(solutions, game, conclude)
  }

  def conclude(matchReports: List[MatchReport]): Unit = {
    // TODO
    status = TournamentStatus.Finished
    observer.receiveTournamentFinished()
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
