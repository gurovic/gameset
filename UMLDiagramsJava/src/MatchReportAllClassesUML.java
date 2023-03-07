import java.util.Map;
import java.util.List;

public class MatchReport {

    private long matchId;
    private Map<long, InvokerReport> invokerReports;
    private Map<long, int> solutionScores;
    private Map<long, int> solutionPlaces;
    private boolean placesOnly;

    public MatchReport (long matchId, Map<long, InvokerReport> invokerReports,
                        Map<long, int> solutionScores, Map<long, int> solutionPlaces, boolean placesOnly) {
        this.matchId = matchId;
        this.invokerReports = invokerReports;
        this.solutionScores = solutionScores;
        this.solutionPlaces = solutionPlaces;
        this.placesOnly = placesOnly;
    }

    public long getMatchId() {
        return matchId;
    }

    public boolean getPlacesOnly() {
        return placesOnly;
    }

    public InvokerReport getSolutionReport(long solutionId) {
        return invokerReports[solutionId];
    }

    public int getSolutionScore(long solutionId) {
        return solutionScores[solutionId];
    }

    public Map<long, int> getSolutionScores() {
        return solutionScores;
    }

    public int getSolutionPlace(long solutionId) {
        return solutionPlaces[solutionId];
    }

    public Map<long, int> getSolutionPlaces() {
        return solutionPlaces;
    }
}

public class Match {
    private List<Solution> solutions;
    private MatchReport report;

    public Match(List<Solution> solutions) {
        this.solutions = solutions;
    }

    private void createReport() {
        matchFinishedObserver.receive(report);
    }

}

public class Solution {
}

public class InvokerReport {
}
