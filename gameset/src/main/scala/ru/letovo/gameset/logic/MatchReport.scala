package ru.letovo.gameset.logic
class MatchReport() {

  var matchId: Long = _
  var solutionScores: Map[Long, Int] = _
  var PlacesOnly: Boolean = _

  var executedWithError: Boolean = _
  var errorSolution: Map[Long, String] = _

}
