package ru.letovo.gameset.logic

import java.util.function.Function


trait TournamentSystem {
  def startTesting(solutions: List[Solution], game: Game, callback: Function[List[MatchReport], _]): Unit

  val id: Long
}

object TournamentSystemRegistry {
  val system = Map(
    1 -> classOf[TournamentSystemRoundRobin],
//    2 -> classOf[TournamentSystemOlympic]
  )
}
//    TournamentSystemRegistry.system(1).getConstructor().newInstance() - create new TournamentSystem object
