package controllers

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import java.nio.file.Paths
import javax.inject._
import models._
import scala.sys.process._
import java.io.File
import compiler.Compiler

import java.io.File
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's games page.
 */
@Singleton
class GamesController @Inject() (val controllerComponents: ControllerComponents,
                                 val dbConfigProvider: DatabaseConfigProvider)
  extends BaseController with HasDatabaseConfigProvider[JdbcProfile] {
  val games = TableQuery[GamesTable]
  val SRC_DIR = "../data/games"
  def index() = Action.async { req =>
    val schema = games.schema ++ TableQuery[CompilationReportsTable].schema ++ TableQuery[MatchReportsTable].schema ++ TableQuery[SolutionsTable].schema ++ TableQuery[ProgramsTable].schema
    db.run(schema.createIfNotExists)
    db.run(games.result)
      .map { allGames =>
        Ok(views.html.games.index(allGames))
      }
  }

  def addGet() = Action {
    Ok(views.html.games.add())
  }

  def run(id: Long, solutionID1: Long, solutionID2: Long) = Action.async { req =>
    val game = db.run(games.filter(_.id === id).result).map(_.head)
    val solution1 = db.run(TableQuery[SolutionsTable].filter(_.id === solutionID1).result).map(_.head)
    val solution2 = db.run(TableQuery[SolutionsTable].filter(_.id === solutionID2).result).map(_.head)

    for {
      g <- game
      s1 <- solution1
      s2 <- solution2
      intZ <- Compiler.compile(g.interactor, Seq("-I", SRC_DIR + "/include"))
      sol1Z <- Compiler.compile(Solution(s1.id, s1.gameID).path, Seq("-I", SRC_DIR + "/include"))
      sol2Z <- Compiler.compile(Solution(s2.id, s2.gameID).path, Seq("-I", SRC_DIR + "/include"))
    } yield {
      val runId = java.util.UUID.randomUUID.toString
      val sol1pipe = "/tmp/pipe" + s1.id.toString + runId
      val sol2pipe = "/tmp/pipe" + s2.id.toString + runId
      println(sol1pipe)
      println(sol2pipe)
      if (new File(sol1pipe ++ "In").exists()) {
        new File(sol1pipe ++ "In").delete()
      }
      if (new File(sol1pipe ++ "Out").exists()) {
        new File(sol1pipe ++ "Out").delete()
      }
      if (new File(sol2pipe ++ "In").exists()) {
        new File(sol2pipe ++ "In").delete()
      }
      if (new File(sol2pipe ++ "Out").exists()) {
        new File(sol2pipe ++ "Out").delete()
      }
      ("mkfifo " ++ sol1pipe ++ "In").!!
      ("mkfifo " ++ sol1pipe ++ "Out").!!
      ("mkfifo " ++ sol2pipe ++ "In").!!
      ("mkfifo " ++ sol2pipe ++ "Out").!!
      (for {
        int <- intZ
        sol1 <- sol1Z
        sol2 <- sol2Z
      } yield {
        val proc1p = Seq(int, sol1pipe ++ "In" ++ ":" ++ sol1pipe ++ "Out", sol2pipe ++ "In" ++ ":" ++ sol2pipe ++ "Out")
        val sol1p = sol1 #< new File(sol1pipe ++ "In") #> new File(sol1pipe ++ "Out")
        val sol2p = sol2 #< new File(sol2pipe ++ "In") #> new File(sol2pipe ++ "Out")
        sol1p.run()
        sol2p.run()
        println("run1")
        Ok(proc1p.!!)
      }).getOrElse(Ok("Compilation error"))
    }
  }

  def addPost() = Action.async(parse.multipartFormData) { req =>
    db.run(
      (games returning games.map(_.id)) += Game(0, "", "", "")
    ).map { id =>
      val path = s"$SRC_DIR/$id/interactor.cpp"
      req.body
        .file("interactor")
        .map { file =>
          new File(s"$SRC_DIR/$id/").mkdirs()
          file.ref.copyTo(Paths.get(path), replace = true)
        }
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
