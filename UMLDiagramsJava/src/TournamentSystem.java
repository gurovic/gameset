import java.util.List;
import java.util.function.Function;

public interface TournamentSystem {
    void startTesting(List<Solution> solutions, Game game, Function callback);
}
