package models
import slick.jdbc.PostgresProfile.api._

case class Solution(id: Int)

class SolutionsTable(tag: Tag) extends Table[Solution](tag, "solutions") {
  def id = column[Int]("id", O.PrimaryKey)

  def * = id <> (Solution.apply, Solution.unapply)
}

val Solutions = TableQuery[SolutionsTable]
