package ru.letovo.gameset.web.controllers

import play.api.libs.Files
import play.api.mvc._
import ru.letovo.gameset.web.models.{GamesTable, SolutionsTable, Tournament, TournamentTable}
import slick.lifted
import slick.jdbc.PostgresProfile.api._

import javax.inject.Inject
import scala.annotation.unused
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class TournamentsController @Inject()(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  import slick.jdbc.PostgresProfile.api.Database

  val db = Database.forConfig("postgres")
  private val gamesTable = lifted.TableQuery[GamesTable]
  private val tournamentsTable = lifted.TableQuery[TournamentTable]
  def creationPage() = Action.async { implicit request: Request[AnyContent] =>
    val gamesFuture = db.run {
      gamesTable.result
    }
    gamesFuture map { games =>
      Ok(views.html.newTournament(games))
    }
  }

  def createTournament(@unused version: Int) = Action.async(parse.formUrlEncoded) { implicit request =>
    val name = request.body.get("name").head.head
    val gameID = request.body.get("gameID").head.head.toLong
    val tournament = db.run {
      (tournamentsTable returning tournamentsTable.map(_.id)
      into ((tournament, id) => tournament.copy(id = id))) += Tournament(0, name, gameID, request.session.get("userID").get.toLong, 1)
    }
    tournament map { tournament =>
      Redirect("/tournaments/" + tournament.id)
    }
  }
}
