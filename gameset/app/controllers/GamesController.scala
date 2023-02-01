package controllers

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import java.nio.file.Paths
import javax.inject._
import models._

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's games page.
 */
@Singleton
class GamesController @Inject() (val controllerComponents: ControllerComponents,
                                 val dbConfigProvider: DatabaseConfigProvider)
  extends BaseController with HasDatabaseConfigProvider[JdbcProfile] {
  val games = TableQuery[GamesTable]
  val SRC_DIR = "/home/gameset/interactor_src"
  def index() = Action.async { req =>

    db.run(games.map { game =>
      (game.name, game.id.toString())
    }.result)
      .map { allGames =>
        Ok(views.html.games.index(allGames))
      }
  }

  def addGet() = Action {
    Ok(views.html.games.add())
  }

  def addPost() = Action.async(parse.multipartFormData) { req =>
    db.run(
      (games returning games.map(_.id)) += Game(0, "", "", "")
    ).map { id =>
      val path = s"$SRC_DIR/$id/interactor.cpp"
      req.body
        .file("interactor")
        .map(
          _.ref.copyTo(Paths.get(path), replace = true)
        )
      val query = for {
        nameS <- req.body.dataParts.get("name")
        name <- nameS.headOption
        rulesS <- req.body.dataParts.get("rules")
        rules <- rulesS.headOption
        game = Game(id, name, path, rules)
        query = db.run(games.filter(_.id === id).update(game))
      } yield query
      query.get
    }.map { _ =>
      Redirect(routes.GamesController.index())
    }
  }
}
