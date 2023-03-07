import java.util.Map;
import java.util.List;

public class MatchReport {

    private long matchId;
    private Map[long, InvokerReport] invokerReports;

    private Map[long, int] solutionScores;
    private Map[long, int] solutionPlaces;

    private boolean placesOnly;

    public MatchReport (private long matchId, Map[long, InvokerReport] invokerReports,
                        Map[long, int] solutionScores, Map[long, int] solutionPlaces, boolean placesOnly) {
        this.matchId = matchId;
        this.invokerReports = invokerReports;
        this.solutionScores = solutionScores;
        this.solutionPlaces = solutionPlaces;
        this.placesOnly = placesOnly;
    }

    public void getMatchId() {
        return matchId;
    }

    public void getPlacesOnly() {
        return placesOnly;
    }

    public InvokerReport getSolutionReport(long solutionId) {
        return invokerReports[solutionId];
    }

    public int getSolutionScore(long solutionId) {
        return solutionScores[solutionId];
    }

    public int getSolutionPlace(long solutionId) {
        return solutionPlaces[solutionId];
    }
}

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

}

public class Solution {
}

public class InvokerReport {
}
