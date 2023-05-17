package ru.letovo.gameset.web.controllers

import play.api.mvc._
import ru.letovo.gameset.web.models.{SolutionsRepository, SolutionsTable, Solution}
import slick.jdbc.PostgresProfile.api._
import slick.lifted

import java.nio.file.{Files, Paths}
import javax.inject._
import scala.annotation.unused
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class SolutionsController @Inject()(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  import slick.jdbc.PostgresProfile.api.Database

  val db = Database.forConfig("postgres")
  val solutionsTable = lifted.TableQuery[SolutionsTable]
  val repo = new SolutionsRepository(db)


  def listSolutions(gameID: Long) = Action { implicit request =>
    val solutionsFuture = repo.findAllByGameID(gameID)

    solutionsFuture.onComplete {
      case Success(solutions) => Ok(views.html.solutions(gameID, solutions))

      case Failure(e) =>
        println("Couldn't obtain solutions")
        e.printStackTrace()
        Redirect(routes.HomeController.index()).flashing("error" -> e.toString)
    }

    Ok("")
  }

  def viewSolution(gameID: Long, solutionID: Long) = Action { implicit request =>
    val solutionFuture = repo.findAllByID(solutionID)

    solutionFuture.onComplete {
      case Success(solution) =>
        if (solution.nonEmpty)
          Ok(views.html.solution(gameID, solution.head))

        println("No such solution")
        Redirect(routes.HomeController.index()).flashing("error" -> "No such solution")

      case Failure(e) =>
        println("Couldn't obtain solution")
        e.printStackTrace()
        Redirect(routes.HomeController.index()).flashing("error" -> e.toString)
    }

    Ok("")
  }

  def newSolution(gameID: Long) = Action { implicit request =>
    Ok(views.html.newSolution(gameID, routes.SolutionsController.uploadSolution(1, gameID)))
  }

  def uploadSolution(@unused version: Int, gameID: Long) = Action(parse.multipartFormData) { implicit request =>
    if (!request.hasBody)
      Redirect(routes.HomeController.index()).flashing("error" -> "No body")

    request.body
      .file("solution")
      .map { solutionFile =>
        if (solutionFile.fileSize > 1024 * 1024) {
          println("File too large")
          Redirect(routes.HomeController.index()).flashing("error" -> "File too large")
        }

        val name = request.body.dataParts.get("name").map(_.head).getOrElse(solutionFile.filename)

        val solutionFuture = repo.newUserSolution(gameID, request.id, name)

        solutionFuture.onComplete {
          case Success(s) =>
            val potentialPath = Paths.get(s.path)
            if (!potentialPath.toFile.exists()) {
              Files.createDirectories(potentialPath.getParent)
              println("Created directory")
            }

            solutionFile.ref.copyTo(potentialPath, replace = true)

            Redirect(routes.SolutionsController.viewSolution(gameID, s.id.getOrElse(-10))).flashing("success" -> "File uploaded")

          case Failure(e) =>
            println("Couldn't create solution")
            e.printStackTrace()
            Redirect(routes.HomeController.index()).flashing("error" -> e.toString)
        }
      }
      .getOrElse {
        println("No file uploaded")
        Redirect(routes.HomeController.index()).flashing("error" -> "Missing file")
      }

    Ok("")
  }

  def editSolution(@unused version: Int, gameID: Long, solutionID: Long) = Action { implicit request =>
    if (!request.hasBody)
      Redirect(routes.HomeController.index()).flashing("error" -> "No body")

    request.body.asJson.map { json =>
      //      json.validate[Solution]

      val newGameID = (json \ "game_id").asOpt[Long]
      val newCreatorID = (json \ "creator_id").asOpt[Long]
      val newSolutionName = (json \ "name").asOpt[String]

      val selectQuery = for {s <- solutionsTable if s.id === solutionID} yield s

      val affectedRowsCount = db.run {
        selectQuery.result.head.map { solution =>
          selectQuery.update(Solution(Some(solutionID), newGameID.getOrElse(solution.gameID),
            newCreatorID.getOrElse(solution.creatorID), newSolutionName.getOrElse(solution.name)))
        }
      }

      affectedRowsCount.onComplete {
        case Success(_) =>
          Redirect(routes.SolutionsController.viewSolution(gameID, solutionID)).flashing("success" -> "Solution updated")
        case Failure(e) =>
          println("Couldn't update solution")
          e.printStackTrace()
          Redirect(routes.HomeController.index()).flashing("error" -> e.toString)
      }
    }.getOrElse {
      println("No JSON body")
      Redirect(routes.HomeController.index()).flashing("error" -> "No JSON body")
    }

    Ok("")
  }

  def deleteSolution(@unused version: Int, @unused gameID: Long, solutionID: Long): Action[AnyContent] = Action { implicit request =>
    /*val affectedRowsCountFuture = db.run {
      val q = for {s <- solutionsTable if s.id === solutionID} yield s

      q.exists.result.map { exists =>
        if (exists) q.delete
        else DBIO.failed(new Exception("No such solution"))
      }
    }*/

    val affectedRowsCountFuture = repo.deleteByID(solutionID)

    affectedRowsCountFuture.onComplete {
      case Success(_) =>
        Redirect(routes.HomeController.index()).flashing("success" -> "Solution deleted")

      case Failure(e) =>
        println("Couldn't delete solution")
        e.printStackTrace()
        Redirect(routes.HomeController.index()).flashing("error" -> e.toString)
    }

    Ok("")
  }

}