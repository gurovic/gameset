package ru.letovo.gameset.logic

trait TournamentFinishedObserver {
  def receiveTournamentFinished(): Unit
}
