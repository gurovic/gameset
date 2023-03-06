import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class Pair {
    // Return a map entry (key-value pair) from the specified values
    public static <T, U> Map.Entry<T, U> of(T first, U second) {
        return new AbstractMap.SimpleEntry<>(first, second);
    }
}

public class TournamentSystemRoundRobin implements TournamentSystem, MatchFinishedObserver {
    private Game game;
    private int matchesCompleted = 0;
    private int matchesNumber = 0;
    private List<Solution> solutions;
    private List<List<MatchReport>> matchReportsMatrix;
    private List<List<Map.Entry<Solution, Integer>>> solutionGroups;


    @Override
    public void startTesting(List<Solution> solutions, Game game, Function callback) {
    }

    private Match[] getNextMatch() {
        return null;
    }

    private List<List<Map.Entry<Solution, Integer>>> generateSolutionGroups() {
        return null;
    }

    private List<List<MatchReport>> generateMatchReportsMatrix() {
        return null;
    }

    private int generateMatchesNumber() {
        return 0;
    }


    @Override
    public void receive(MatchReport matchReport) {
    }
}