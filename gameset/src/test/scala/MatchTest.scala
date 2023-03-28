import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class MatchTest extends AnyFunSuite with BeforeAndAfter {

  before {
    var match_: Match = new Match()

    var observer: MatchFinishedObserver = _
    val invokers = Array(new Invoker("3", Seq("3")), new Invoker("2", Seq("2")), new Invoker("1", Seq("1")))
    val roots = Array(new String(""), new String("test"), new String("test1/test2"))
    val paths = Array(new String(""), new String("test"), new String("test1/test2"))
  }

  test("Match constructor") {
  }

  test("run") {
    match_.run(observer)
  }

  test("prepareInvokers") {
    match_.prepareInvokers()
  }

  test("prepareInteractor") {
    for (i <- 0 to 2) {
      match_.prepareInteractor(roots[i])
    }
  }

  test("setupInvokers") {
    match_.prepareInvokers()
  }

  test("initInvokerInOutNames") {
    for (i <- List(0, 2); j <- List(0, 2)) {
      match_.initInvokerInOutNames(invokers[i], root[j])
    }
  }

  test("createPipe") {
    for (i <- 0 to 2) {
      match_.createPipe(paths[i])
    }
  }

  test("createReport") {
    match_.createReport()
  }
}