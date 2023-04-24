package ru.letovo.gameset.web.models

import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.Json
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class MatchReport(matchId: Long, solutionScores: Map[Long, Int], PlacesOnly: Boolean, ErrorCode: Boolean, ErrorSolutions: List[Int]) {

}

object MatchReport{
  implicit val reportformat = Json.format[MatchReport]
}

class MatchReportsTable(tag: Tag) extends Table[MatchReport](tag, "reports") {

  /**
   * This is the tables default "projection".
   *
   * It defines how the columns are converted to and from the Solution object.
   *
   * In this case, we are simply passing the ID and gameID parameters to the Solution case classes
   * apply and unapply methods.
   */
  def * = (matchId, solutionScores, PlacesOnly, ErrorCode, ErrorSolutions) <> ((MatchReport.apply _).tupled, MatchReport.unapply)

  /** The ID column, which is the primary key, and auto incremented */
  def matchId = column[Long]("matchId")

  def solutionScores =  column[Map[Long, Int]]("solutionScores")
  def PlacesOnly = column[Boolean]("PlacesOnly")

  def ErrorCode = column[Boolean]("ErrorCode")
  def ErrorSolutions = column[List[Int]]("ErrorSolutions")

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
  private val report = TableQuery[MatchReportsTable]

  /**
   * Create a solution with the given gameID.
   *
   * This is an asynchronous operation, it will return a future of the created solution, which can be used to obtain the
   * id for that solution.
   */
  def create(matchId: Long, solutionScores: Map[Long, Int], PlacesOnly: Boolean, ErrorCode: Boolean, ErrorSolutions: List[Int]): Future[MatchReport] = db.run {
    // We create a projection of just the gameID column, since we're not inserting a value for the id column
    (report.map(p => (p.matchId)),
      report.map(p => (p.solutionScores)),
      report.map(p=> (p.PlacesOnly)),
      report.map(p=> (p.ErrorCode)),
      report.map(p=> (p.ErrorSolutions))

      // Now define it to return the id, because we want to know what id was generated for the person

      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id

      // And finally, insert the person into the database
      )+= matchId
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[MatchReport]] = db.run {
    report.result
  }

  def getMatchReport(matchId: Long) = db.run {
    report.filter(_.matchId === matchId).result
  }

  /**
   * Here we define the table. It will have a name of people
   */
}
