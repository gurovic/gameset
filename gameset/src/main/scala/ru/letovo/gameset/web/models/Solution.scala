package ru.letovo.gameset.web.models

import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.Json
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class Solution(id: Long, gameID: Long) {
  def path = s"../data/games/$gameID/user-solutions/$id.cpp"
}

object Solution {
  implicit val solutionFormat = Json.format[Solution]
}

class SolutionsTable(tag: Tag) extends Table[Solution](tag, "solutions") {

  /**
   * This is the tables default "projection".
   *
   * It defines how the columns are converted to and from the Solution object.
   *
   * In this case, we are simply passing the ID and gameID parameters to the Solution case classes
   * apply and unapply methods.
   */
  def * = (ID, gameID) <> ((Solution.apply _).tupled, Solution.unapply)

  /** The ID column, which is the primary key, and auto incremented */
  def ID = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def gameID = column[Long]("game_id")
}

/**
 * A repository for solutions.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class SolutionsRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.

  import dbConfig._
  import profile.api._

  /**
   * The starting point for all queries on the solutions table.
   */
  private val solution = TableQuery[SolutionsTable]

  /**
   * Create a solution with the given gameID.
   *
   * This is an asynchronous operation, it will return a future of the created solution, which can be used to obtain the
   * id for that solution.
   */
  def create(gameID: Long): Future[Solution] = db.run {
    // We create a projection of just the gameID column, since we're not inserting a value for the id column
    (solution.map(s => s.gameID)
      // Now define it to return the id, because we want to know what id was generated for the person
      returning solution.map(_.ID)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((gameID, id) => Solution(id, gameID))
      // And finally, insert the solution into the database
      ) += gameID
  }

  def list(): Future[Seq[Solution]] = db.run {
    solution.result
  }

  def getSolution(id: Long) = db.run {
    solution.filter(_.ID === id).result
  }
}
