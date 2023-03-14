import java.util
import java.util.function.Function


trait TournamentSystem {
  def startTesting(solutions: util.List[Solution], game: Game, callback: Function[util.List[util.List[MatchReport]], _]): Unit
}
