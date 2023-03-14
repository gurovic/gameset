import org.scalatest.funsuite.AnyFunSuite

class MatchReportTest extends AnyFunSuite {

  test("MatchReport constructor") {

    val match_report = new MatchReport
    match_report.matchId = 100
    assert(match_report.matchId === 100)
  }


}