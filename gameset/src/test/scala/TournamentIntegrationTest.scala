import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class TournamentIntegrationTest extends AnyFunSuite with BeforeAndAfter {
  test("Tournament execution") {
    val game = new Game()
    val testFilesPath = System.getProperty("user.dir") + "/src/test/scala/test_game/"
    game.interactorPath = new Compiler().compile(testFilesPath + "interactor.cpp")

    val tournament = new Tournament(
      "Test tour",
      game,
      new TournamentSystemRoundRobin(),
      2
    )
    val solution1 = new Solution()
    solution1.path = new Compiler().compile(testFilesPath + "mtdf_depth_limited.cpp")
    val solution2 = new Solution()
    solution2.path = new Compiler().compile(testFilesPath + "negamax_depth_limited.cpp")

    tournament.open()
    tournament.addSolution(solution1)
    tournament.addSolution(solution2)
    tournament.close()
  }
}
