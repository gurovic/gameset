package models

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag
import models._
import ru.letovo.gameset.web.models.{Solution, SolutionsTable}
//import ru.letovo.gameset.web.models.User

case class Game(id: Long,
                name: String, // -> Path to binary
                interactor: String, // -> Path to binary
                rules: String,
                author_solutionID: Long,
                dumb_SolutionID: Long,
                authorID: Long,
               ) // TODO: Store rules in a better format


class GamesTable(tag: Tag) extends Table[Game](tag, "games") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def interactor = column[String]("interactor")

  def rules = column[String]("rules")

  def author_solutionID = column[Long]("author_solutionID")
  def author_solution = foreignKey("AUTHOR_SOLUTION", author_solutionID, TableQuery[SolutionsTable])(_.ID)

  def dumb_solutionID = column[Long]("dumb_solutionID")

  def dumb_solution = foreignKey("DUMB_SOLUTION", dumb_solutionID, TableQuery[SolutionsTable])(_.ID)
  def authorID = column[Long]("author_id")
//  def author = foreignKey("AUTHOR", author_ID, TableQuery[User])(_.id)

  def * = (id, name, interactor, rules, author_solutionID, dumb_solutionID, authorID) <> (Game.tupled, Game.unapply)
}