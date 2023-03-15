import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

// интеграционные без моков

class MatchTest extends AnyFunSuite with BeforeAndAfter {

  var match_: Match = _
  var observer: MatchFinishedObserver = _
  var invoker: Invoker = _
  var path: String = _

  before {}

  test("run") {
    match_.run(observer)
  }

  test("prepareInvokers") {
    match_.prepareInvokers(path)
  }

  test("prepareInteractor") {
    match_.prepareInteractor(path)
  }

  test("setupInvokers") {
    match_.prepareInvokers()
  }

  test("initInvokerInOutNames") {
    match_.initInvokerInOutNames(invoker, path)
  }

  test("createPipe") {
    match_.createPipe(path)
  }

  test("createReport") {
    match_.createReport()
  }
}