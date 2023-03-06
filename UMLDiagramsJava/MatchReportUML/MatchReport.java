import java.util.Map;

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
    } /* just in case; may be not needed or changed */

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

    public void submitToTournamentSystem() {
        /* Submit the report; placeholder, may be executed by Match only */
        System.out.println("Report submitted")
        return;
    }
}
