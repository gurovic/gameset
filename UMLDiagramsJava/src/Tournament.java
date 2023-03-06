import java.util.Date;

public class Tournament {
    private String name; // name of the tournament
    private Date timestamp; // date and time of the tournament
    private int numPlayers; // number of players in the tournament
    private int numMatchesPlayed; // number of matches that have been played
    private MatchReport[] matchReports;
    private TournamentSystem tournamentSystem;
    private Game game;
    private MatchQueue matchQueue;


    public Tournament(String name, String date, String time, int numPlayers) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.numPlayers = numPlayers;
        this.numMatchesPlayed = 0;
        this.matchReports = new MatchReport[numPlayers];
    }

    public int addMatchReport(MatchReport matchReport) {
        // returns 0 if match report is added successfully
        // returns 1 if match report is not added successfully
        // returns 2 if match report is not added successfully because of a duplicate match report

        matchReports[numMatchesPlayed] = matchReport;
        numMatchesPlayed++;
        return 0;
    }

    public MatchReport getMatchReport(int index) {
        return matchReports[index];
    }

    public MatchReport[] getMatchReportsByPlayer(String player) {
        MatchReport[] matchReportsByPlayer = new MatchReport[numMatchesPlayed];
        int numMatchReportsByPlayer = 0;
        for (int i = 0; i < numMatchesPlayed; i++) {
            if (matchReports[i].getWinner().equals(player) || matchReports[i].getLoser().equals(player)) {
                matchReportsByPlayer[numMatchReportsByPlayer] = matchReports[i];
                numMatchReportsByPlayer++;
            }
        }
        return matchReportsByPlayer;
    }

    public MatchReport[] getMatchReportsByWinner(String winner) {
        MatchReport[] matchReportsByWinner = new MatchReport[numMatchesPlayed];
        int numMatchReportsByWinner = 0;
        for (int i = 0; i < numMatchesPlayed; i++) {
            if (matchReports[i].getWinner().equals(winner)) {
                matchReportsByWinner[numMatchReportsByWinner] = matchReports[i];
                numMatchReportsByWinner++;
            }
        }
        return matchReportsByWinner;
    }

    public MatchReport[] getMatchReportsByLoser(String loser) {
        MatchReport[] matchReportsByLoser = new MatchReport[numMatchesPlayed];
        int numMatchReportsByLoser = 0;
        for (int i = 0; i < numMatchesPlayed; i++) {
            if (matchReports[i].getLoser().equals(loser)) {
                matchReportsByLoser[numMatchReportsByLoser] = matchReports[i];
                numMatchReportsByLoser++;
            }
        }
        return matchReportsByLoser;
    }

    public int addMatchToQueue(Match match) {
        // add match to the MatchQueue
        // returns 0 if match is added successfully
        // returns 1 if match is not added successfully
        // returns 2 if match is not added successfully because of a duplicate match

        return 0;
    }
}