class MatchReport {
    private String winner;
    private String loser;
    private int winnerScore;
    private int loserScore;

    public MatchReport(String winner, String loser, int winnerScore, int loserScore) {
        this.winner = winner;
        this.loser = loser;
        this.winnerScore = winnerScore;
        this.loserScore = loserScore;
    }

    public String getWinner() {
        return winner;
    }

    public String getLoser() {
        return loser;
    }

    public int getWinnerScore() {
        return winnerScore;
    }

    public int getLoserScore() {
        return loserScore;
    }
}
