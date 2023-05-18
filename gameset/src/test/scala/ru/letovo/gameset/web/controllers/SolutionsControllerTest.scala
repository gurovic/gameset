package ru.letovo.gameset.web.controllers

import org.mockito.ArgumentMatchers.any
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.{ExecutionContext, Future}

class SolutionsControllerTest extends PlaySpec with Results {
  val ec: ExecutionContext = ExecutionContext.global

  "Solutions page#listSolutions" should {
    "should be valid" in {
      val controller = new SolutionsController(Helpers.stubControllerComponents())(ec)
      val result: Future[Result] = controller.listSolutions(any[Long]).apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

  "Solutions page#viewSolution" should {
    "should be valid" in {
      val controller = new SolutionsController(Helpers.stubControllerComponents())(ec)
      val result: Future[Result] = controller.viewSolution(any[Long], any[Long]).apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

  "Solutions page#newSolution" should {
    "should be valid" in {
      val controller = new SolutionsController(Helpers.stubControllerComponents())(ec)
      val result: Future[Result] = controller.newSolution(any[Long]).apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

  "Solutions page#editSolution" should {
    "should be valid" in {
      val controller = new SolutionsController(Helpers.stubControllerComponents())(ec)
      val result: Future[Result] = controller.editSolution(1, any[Long], any[Long]).apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

  "Solutions page#deleteSolution" should {
    "should be valid" in {
      val controller = new SolutionsController(Helpers.stubControllerComponents())(ec)
      val result: Future[Result] = controller.deleteSolution(1, any[Long], any[Long]).apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText mustBe "ok"
    }
  }

}