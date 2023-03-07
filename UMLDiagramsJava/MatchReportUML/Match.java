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
