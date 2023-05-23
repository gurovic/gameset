package ru.letovo.gameset.web.models

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


class SolutionRepositoryTest extends AnyWordSpec with Matchers {
  val sampleAuthorSolution: Solution = Solution(Some(-1), 1, 1, "testAuthorSolution")
  val sampleUserSolution: Solution = Solution(Some(10), 1, 1, "testUserSolution")

  "Solution#path" should {
    "return correct path" in {
      sampleAuthorSolution.path mustBe "../data/games/1/user-solutions/-1"
      sampleUserSolution.path mustBe "../data/games/1/user-solutions/10"
    }
  }

  "Solution#isAuthorSolution" should {
    "be true when the id is -1" in {
      sampleAuthorSolution.isAuthorSolution mustBe true
    }
  }

  "Solution#isAuthorSolution" should {
    "be false when the id is not -1" in {
      sampleUserSolution.isAuthorSolution mustBe false
    }
  }

}


class SolutionModelTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar with Matchers {
  var repo: SolutionsRepository = _
  val ec: ExecutionContext = ExecutionContext.global
  private val sampleSolution = Solution(Some(-1), 10, 2, "test")

  before {
    repo = mock[SolutionsRepository]
  }

  test("newUserSolution should return newly created solution") {
    when(repo.newUserSolution(10, 2, "test")).thenReturn(Future.successful(sampleSolution))

    repo.newUserSolution(10, 2, "test").onComplete {
      case Success(s) => s mustBe sampleSolution
      case Failure(e) => fail(e)
    }(ec)

    verify(repo).newUserSolution(10, 2, "test")
  }

  test("findAllByID should return solution with the given id") {
    when(repo.findAllByID(-1)).thenReturn(Future.successful(Seq[Solution](sampleSolution)))

    repo.findAllByID(-1).onComplete {
      case Success(s) => s.head mustBe sampleSolution
      case Failure(e) => fail(e)
    }(ec)

    verify(repo).findAllByID(-1)
  }

  test("findAllByTournamentID should return solutions with the given tournament id") {
    when(repo.findAllByTournamentID(10)).thenReturn(Future.successful(Seq[Solution](sampleSolution)))
    repo.findAllByTournamentID(10).onComplete {
      case Success(s) => s.head mustBe sampleSolution
      case Failure(e) => fail(e)
    }(ec)
    verify(repo).findAllByTournamentID(10)
  }

  test("deleteByID should return 1 when the solution is deleted") {
    when(repo.deleteByID(-1)).thenReturn(Future.successful(any[Int]))
    repo.deleteByID(-1).onComplete {
      case Success(s) => s mustBe 0
      case Failure(e) => fail(e)
    }(ec)
    verify(repo).deleteByID(-1)
  }

}
