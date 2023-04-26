package ru.letovo.gameset.web.models

import play.api.libs.json.Json
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class Solution(id: Long, gameID: Long) {

  /**
   * Author's solutions uses (-1) ID
   */

  def path = s"../data/games/$gameID/user-solutions/$id"
}

object Solution {
  implicit val solutionFormat = Json.format[Solution]
}

class SolutionsTable(tag: Tag) extends Table[Solution](tag, "solutions") {

  /**
   * This is the tables default "projection".
   *
   * It defines how the columns are converted to and from the Solution object.
   */

  def * = (id, gameID) <> ((Solution.apply _).tupled, Solution.unapply)

  /** The ID column, which is the primary key, and auto incremented */
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def gameID = column[Long]("gameID")
}
