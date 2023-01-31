package controllers

import models.SolutionsRepository
import play.api.mvc._
import play.twirl.api.{Html, Txt}

import java.nio.file.{Files, Paths}
import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class SolutionsController @Inject()(val repo: SolutionsRepository, val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  def newSolution(gameID: Long) = Action {
    Ok(views.html.main("Solution upload")(Html(
      s"""
         |<form action="${routes.SolutionsController.uploadSolution(gameID).url}" method="post" enctype="multipart/form-data">
         |
         |            <p>
         |                Select a file containing solution: <input type="file" name="solution" />
         |            </p>
         |
         |            <input type="submit" value="Upload the solution" />
         |        </form>
         |""".stripMargin))
    )
  }

  def viewSolution(gameID: Long, solutionID: Long) = Action {
    Ok(Txt(s"file uploaded for game $gameID. ID is $solutionID"))
  }

  def uploadSolution(gameID: Long) = Action(parse.multipartFormData) { request =>
    request.body
      .file("solution")
      .map { solutionFile =>
        // only get the last part of the filename
        // otherwise someone can send a path like ../../tmp/foo/bar.txt to write to other files on the system
        val filename = Paths.get(solutionFile.filename).getFileName

        if (solutionFile.fileSize > 1024 * 1024) {
          println("File too large")
          Redirect(routes.HomeController.index()).flashing("error" -> "File too large")
        }

        repo.create(gameID).map { solution =>
          repo.getPath().map { path =>
            val potentialPath = Paths.get(path.head)

            if (!potentialPath.toFile.exists()) {
              Files.createDirectories(potentialPath)
              println("Created directory")
            }

            solutionFile.ref.copyTo(Paths.get(s"$potentialPath/$filename"), replace = true)

            Redirect(routes.SolutionsController.viewSolution(gameID, solution.id)).flashing("success" -> "File uploaded")
          }
        }
      }
      .getOrElse {
        println("No file uploaded")
        Redirect(routes.HomeController.index()).flashing("error" -> "Missing file")
      }
    Ok("File uploaded")
  }
}
