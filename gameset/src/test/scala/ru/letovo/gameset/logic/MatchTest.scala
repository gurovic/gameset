/*
package ru.letovo.gameset.logic

import org.scalatest.{BeforeAndAfterAll, PrivateMethodTester}
import org.scalatest.funsuite.AnyFunSuite
import ru.letovo.gameset.web.models.Solution
import org.mockito.ArgumentMatchers.any

import java.io.File
import java.nio.file.Paths
import java.util.UUID
import scala.sys.process.Process

package object testingUtils {
  def isPipe(filename: String): Boolean = {
    new File(filename).exists() && Process(Seq("/bin/stat", "-c", "%A", filename)).lazyLines.head(0) == 'p'
  }

  def isTextFile(filename: String): Boolean = {
    new File(filename).isFile
  }

  def removeFile(filename: String): Unit = {
    new File(filename).delete()
    var parent = Paths.get(filename).getParent
    // this refuses to delete non-empty directories
    while (parent != null && new File(parent.toString).delete()) {
      parent = parent.getParent
    }
  }

  def castToInvokerArray(invokers_ref: AnyRef): Array[Invoker] = {
    // in MatchTest class name "Invoker" is shadowed by some Invoker in PrivateMethodTester
    invokers_ref.asInstanceOf[Array[Invoker]]
  }
}

class MatchTest extends AnyFunSuite with BeforeAndAfterAll with PrivateMethodTester {

  private var match_ : Match = _
  private val testFileName: String = UUID.randomUUID().toString
  private val invokersField = classOf[Match].getDeclaredField("invokers")

  override protected def beforeAll(): Unit = {
    val solution = Solution(Some(any[Long]), any[Long], any[Long], any[String])
    match_ = new Match(List(solution, solution), Game())
    invokersField.setAccessible(true)
  }

  override protected def afterAll(): Unit = {
    new File(testFileName).delete()
    invokersField.setAccessible(true)
    val invokers_ref = invokersField.get(match_)

    for (invoker <- testingUtils.castToInvokerArray(invokers_ref)) {
      testingUtils.removeFile(invoker.stdin.get)
      testingUtils.removeFile(invoker.stdout.get)
    }
  }

  test("createPipe") {
    match_ invokePrivate PrivateMethod[Unit](Symbol("createPipe"))(testFileName)
    assert(testingUtils.isPipe(testFileName))
  }

  test("prepareInvokers") {
    match_ invokePrivate PrivateMethod[Unit](Symbol("prepareInvokers"))()

    val invokers = testingUtils.castToInvokerArray(invokersField.get(match_))

    val invokerArgvField = invokers.head.getClass.getDeclaredField("Invoker$$argv")
    invokerArgvField.setAccessible(true)
    val interactorArgv = invokerArgvField.get(invokers.last).asInstanceOf[Seq[String]]
    assert(interactorArgv.length + 1 == invokers.length)

    for (i <- interactorArgv.indices) {
      assert(invokers(i).stdin.isDefined && invokers(i).stdout.isDefined && invokers(i).stderr.isEmpty)
      assert(interactorArgv(i) == invokers(i).stdin.get + ":" + invokers(i).stdout.get)
    }
  }

  test("setupInvokers") {
    match_ invokePrivate PrivateMethod[Unit](Symbol("setupInvokers"))()
    val invokers = testingUtils.castToInvokerArray(invokersField.get(match_))
    for (invoker <- invokers.dropRight(1)) {
      assert(testingUtils.isPipe(invoker.stdin.get) && testingUtils.isPipe(invoker.stdout.get))
    }
    assert(testingUtils.isTextFile(invokers.last.stdin.get))
    assert(testingUtils.isTextFile(invokers.last.stdout.get))
  }
}
*/
