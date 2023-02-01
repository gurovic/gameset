package models

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag
import models._

case class MatchReport (id: Long,
                        matchDuration: Long,
                        matchStartTime: Long,
                        interactorLog: String,
                        participatingSolution1: Long, // TODO: store solutions ids in a better format
                        participatingSolution2: Long,
                        winner: Long
)

class MatchReportsTable(tag: Tag) extends Table[MatchReport](tag, "matchReports") {
  def id = column[Long]("id", O.PrimaryKey)

  def matchDuration = column[Long]("match_duration")

  def matchStartTime = column[Long]("match_start_time")

  def interactorLog = column[String]("interactor_log")

  def participatingSolution1 = column[Long]("participating_solution1")

  def participatingSolution1Fk = foreignKey("participating_solution1_fk", participatingSolution1, TableQuery[SolutionsTable])(_.id)

  def participatingSolution2 = column[Long]("participating_solution2")

  def participatingSolution2Fk = foreignKey("participating_solution2_fk", participatingSolution2, TableQuery[SolutionsTable])(_.id)

  def winner = column[Long]("winner")

  def winnerFk = foreignKey("winner_fk", winner, TableQuery[SolutionsTable])(_.id)

  def * = (id, matchDuration, matchStartTime, interactorLog, participatingSolution1, participatingSolution2, winner) <> (MatchReport.tupled, MatchReport.unapply)
}