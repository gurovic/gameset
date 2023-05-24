package ru.letovo.gameset.web.models

import org.mockito.Mockito.{verify, when}
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import ru.letovo.gameset.logic.Config.videos_root
import ru.letovo.gameset.logic.{RenderConfig, ViewportSize}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success}

class VideoDAOTest extends AnyWordSpec with BeforeAndAfter with MockitoSugar with Matchers {
  private val testId = 1
  private val testTime = 1515
  private val testConfig = RenderConfig(ViewportSize(512, 512), 15, 15, 1)
  private val video: Video = Video(testId, testTime, testConfig)

  "Video#path" should {
    "return correct path" in {
      video.path mustBe videos_root + "/" + testId
    }
  }
}

class VideoModelTest extends AnyFunSuite with BeforeAndAfter with MockitoSugar with Matchers {
  var repo: VideoDAO = _
  private val testId = 1
  private val testTime = 1515
  private val testConfig = RenderConfig(ViewportSize(512, 512), 15, 15, 1)
  val ec: ExecutionContext = ExecutionContext.global
  private val sampleVideo = Video(testId, testTime, testConfig)

  before {
    repo = mock[VideoDAO]
  }

  test("video adding to database") {
    when(repo.save(sampleVideo)).thenReturn(Future.successful(sampleVideo))

    repo.save(sampleVideo).onComplete {
      case Success(s) => s mustBe sampleVideo
      case Failure(e) => fail(e)
    }(ec)

    verify(repo).save(sampleVideo)
  }

  test("video searching by id") {
    Video.apply(testId, testTime, testConfig)
    when(repo.findByID(testId)).thenReturn(Future.successful(sampleVideo))

    repo.findByID(testId).onComplete {
      case Success(s) => s mustBe sampleVideo
      case Failure(e) => fail(e)
    }(ec)

    verify(repo).findByID(testId)
  }
}
