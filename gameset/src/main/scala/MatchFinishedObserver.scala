trait MatchFinishedObserver {
  def receive(matchReport: MatchReport): Unit
}
