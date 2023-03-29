import java.util.AbstractMap;
import java.util.List;

public class Match {
    private List<AbstractMap.SimpleEntry<Integer, Solution>> solutions;
    private Game game;
    private Invoker[] invokers;
    private MatchFinishedObserver matchFinishedObserver;
    private MatchReport report;

    public Match(List<AbstractMap.SimpleEntry<Integer, Solution>> solutions, Game game) {
        this.solutions = solutions;
        this.game = game;
    }

    public void run(MatchFinishedObserver observer) {
        this.matchFinishedObserver = observer;
        InvokerPool().getInstance().addToPool(
                new InvokerRequest(
                        invokers,
                        createReport,
                        Some(setupInvokers)
                )
        )
    }

    private void prepareInvokers() {

    }

    private void prepareInteractor(String root) {
    }

    private void setupInvokers() {

    }

    private void createPipe(String path) {
    }


    private void createReport() {
        matchFinishedObserver.receive(report);
    }
}
