package ru.letovo.gameset.logic

import java.util.function.Function


trait TournamentSystem {
  def startTesting(solutions: List[Solution], game: Game, callback: Function[List[MatchReport], _]): Unit

  val id: Long
}

object TournamentSystemRegistry {
  val system: Seq[Class[_ <: TournamentSystem]] = Seq(classOf[TournamentSystemRoundRobin])
  //    classOf[TournamentSystemOlympic]
}
