import java.util.List;

public class Match {
    private int gameId;

    private List<Solution> solutions;

    private MatchReport report;

    public Match(int gameId, List<Solution> solutions) {
        this.gameId = gameId;
        this.solutions = solutions;
    }

    public void run(List<Invoker> invokers) {
        createReport("path");
    }

    private void createReport(String interactorOutputPath) {
        report = new MatchReport();
        submitReportCreated();
    }

    private void submitReportCreated() {
        report.submitToTournamentSystem();
        System.out.println("Report created");
    }

    public MatchReport getReport() {
        return report;
    }
}