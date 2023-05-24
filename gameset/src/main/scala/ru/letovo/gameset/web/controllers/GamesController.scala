package ru.letovo.gameset.web

import models.{Game, GamesTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import play.api.mvc._

import javax.inject._
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class GamesController @Inject() (val controllerComponents: ControllerComponents,
                                 val dbConfigProvider: DatabaseConfigProvider)
  extends BaseController with HasDatabaseConfigProvider[JdbcProfile] {
  val game_table = TableQuery[GamesTable]



  def getGames(): Future[Seq[Game]] =  db.run{
    game_table.result
  }


  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.games(getGames()))
  }
}