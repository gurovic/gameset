package ru.letovo.gameset.logic

object TournamentStatus extends Enumeration {
  type TournamentStatus = Value
  val Pending, Opened, Testing, Finished = Value
}
