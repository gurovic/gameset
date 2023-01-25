package controllers

import play.api.mvc._
import play.twirl.api.{Html, Txt}

import java.nio.file.{Files, Paths}
import javax.inject._
import scala.util.Random


@Singleton
class SolutionsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def newSolution(gameID: Long) = Action {
    val newSolutionID = Random.nextInt()

    Ok(views.html.main("Solution upload")(Html(
      s"""
         |<form action="${routes.SolutionsController.uploadSolution(gameID, newSolutionID).url}" method="post" enctype="multipart/form-data">
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

  def uploadSolution(gameID: Long, solutionID: Long) = Action(parse.multipartFormData) { request =>
    request.body
      .file("solution")
      .map { solution =>
        // only get the last part of the filename
        // otherwise someone can send a path like ../../tmp/foo/bar.txt to write to other files on the system
        val filename = Paths.get(solution.filename).getFileName
        if (solution.fileSize > 1024 * 1024) {
          println("File too large")
          Redirect(routes.HomeController.index()).flashing("error" -> "File too large")
        }

        val potentialPath = Paths.get(s"/tmp/gameset/game/$gameID/user-solutions/$solutionID")
        if (!potentialPath.toFile.exists()) {
          Files.createDirectories(potentialPath)
          println("Created directory")
        }

        solution.ref.copyTo(Paths.get(s"$potentialPath/$filename"), replace = true)
        Redirect(routes.SolutionsController.viewSolution(gameID, solutionID)).flashing("success" -> "File uploaded")
      }
      .getOrElse {
        println("No file uploaded")
        Redirect(routes.HomeController.index()).flashing("error" -> "Missing file")
      }
  }

}
