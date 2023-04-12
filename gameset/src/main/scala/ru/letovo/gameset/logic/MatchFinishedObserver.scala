package ru.letovo.gameset.logic

trait MatchFinishedObserver {
  def receive(matchReport: MatchReport): Unit
}
