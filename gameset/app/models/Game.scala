package models

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag
import models._

case class Game(id: Int,
                name: String,
                interactor: Int, // -> Program
                replayRenderer: Int, // -> Program TODO: Move into specific class?
                dumbSolution: Int, // -> Solution
                rules: String) // TODO: Store rules in a better format


class GamesTable(tag: Tag) extends Table[Game](tag, "games") {
  def id = column[Int]("id", O.PrimaryKey)

  def name = column[String]("name")

  def interactor = column[Int]("interactor")

  def interactorFk = foreignKey("interactor_fk", interactor, TableQuery[ProgramsTable])(_.id)

  def replayRenderer = column[Int]("replay_renderer")

  def replayRendererFk = foreignKey("replay_renderer_fk", replayRenderer, TableQuery[ProgramsTable])(_.id)

  def dumbSolution = column[Int]("dumb_solution")

  def dumbSolutionFk = foreignKey("dumb_solution_fk", dumbSolution, TableQuery[SolutionsTable])(_.id)

  def rules = column[String]("rules")

  def * = (id, name, interactor, replayRenderer, dumbSolution, rules) <> (Game.tupled, Game.unapply)
}