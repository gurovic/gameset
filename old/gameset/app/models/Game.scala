package models

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag
import models._

case class Game(id: Long,
                name: String, // -> Path to binary
                interactor: String, // -> Path to binary
                rules: String) // TODO: Store rules in a better format


class GamesTable(tag: Tag) extends Table[Game](tag, "games") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def interactor = column[String]("interactor")

  // def dumbSolution = column[Long]("dumb_solution")

  // def dumbSolutionFk = foreignKey("dumb_solution_fk", dumbSolution, TableQuery[SolutionsTable])(_.id)

  def rules = column[String]("rules")

  def * = (id, name, interactor, rules) <> (Game.tupled, Game.unapply)
}
