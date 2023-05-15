package ru.letovo.gameset.web.controllers

import play.api.mvc._
import ru.letovo.gameset.web.models.{Tournament, TournamentsTable}
import slick.jdbc.PostgresProfile.api._
import slick.lifted

import java.nio.file.{Files, Paths}
import javax.inject._
import scala.annotation.unused
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class TournamentsController @Inject()(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  import slick.jdbc.PostgresProfile.api.Database

  val db = Database.forConfig("postgres")
  val tournamentsTable = lifted.TableQuery[TournamentsTable]


  def listTournaments() = Action { implicit request =>
    val tournamentsFuture = db.run {
      tournamentsTable.result
    }

    tournamentsFuture.onComplete {
      case Success(tournaments) => Ok(views.html.tournaments(tournaments))

      case Failure(e) =>
        println("Couldn't obtain tournaments")
        e.printStackTrace()
        Redirect(routes.HomeController.index()).flashing("error" -> e.toString)
    }

    Ok("")
  }

  def viewTournament(tournamentID: Long) = Action { implicit request =>
    val tournamentFuture = db.run {
      tournamentsTable.result
    }

    tournamentFuture.onComplete {
      case Success(tournament) =>
        if (tournament.nonEmpty)
          Ok(views.html.tournament(tournament.head))

        println("No such tournament")
        Redirect(routes.HomeController.index()).flashing("error" -> "No such tournament")

      case Failure(e) =>
        println("Couldn't obtain tournament")
        e.printStackTrace()
        Redirect(routes.HomeController.index()).flashing("error" -> e.toString)
    }

    Ok("")
  }

  def newTournament() = Action { implicit request =>
    Ok(views.html.newTournament(routes.TournamentsController.uploadTournament(1)))
  }

  def uploadTournament(@unused version: Int) = Action(parse.multipartFormData) { implicit request =>
    Ok("")
  }

  def editTournament(@unused version: Int, tournamentID: Long) = Action { implicit request =>
    Ok("")
  }

  def deleteTournament(@unused version: Int, tournamentID: Long): Action[AnyContent] = Action { implicit request =>
    val affectedRowsCount = db.run {
      val q = for {s <- tournamentsTable if s.id === tournamentID} yield s

      q.exists.result.map { exists =>
        if (exists) q.delete
        else DBIO.failed(new Exception("No such tournament"))
      }
    }

    affectedRowsCount.onComplete {
      case Success(_) =>
        Redirect(routes.HomeController.index()).flashing("success" -> "Tournament deleted")

      case Failure(e) =>
        println("Couldn't delete tournament")
        e.printStackTrace()
        Redirect(routes.HomeController.index()).flashing("error" -> e.toString)
    }

    Ok("")
  }

}