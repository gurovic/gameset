import java.util.List;

public class Match {
    private List<Solution> solutions;
    private Game game;
    private MatchFinishedObserver matchFinishedObserver;
    private MatchReport report;

    public Match(List<Solution> solutions, Game game) {
        this.solutions = solutions;
        this.game = game;
    }

    public void run(MatchFinishedObserver observer) {
        /* placeholder */
    }

    private void createReport() {
        matchFinishedObserver.receive(report);
    }

    private void submitReportToTournamentSystem() {
        report.submitToTournamentSystem();
    }
}