package models

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag
import models._

case class MatchReport (id: Int,
                        matchDuration: Int,
                        matchStartTime: Int,
                        interactorLog: String,
                        participatingSolution1: Int, // TODO: store solutions ids in a better format
                        participatingSolution2: Int,
                        winner: Int
)

class MatchReportsTable(tag: Tag) extends Table[MatchReport](tag, "matchReports") {
  def id = column[Int]("id", O.PrimaryKey)

  def matchDuration = column[Int]("match_duration")

  def matchStartTime = column[Int]("match_start_time")

  def interactorLog = column[String]("interactor_log")

  def participatingSolution1 = column[Int]("participating_solution1")

  def participatingSolution1Fk = foreignKey("participating_solution1_fk", participatingSolution1, TableQuery[SolutionsTable])(_.id)

  def participatingSolution2 = column[Int]("participating_solution2")

  def participatingSolution2Fk = foreignKey("participating_solution2_fk", participatingSolution2, TableQuery[SolutionsTable])(_.id)

  def winner = column[Int]("winner")

  def winnerFk = foreignKey("winner_fk", winner, TableQuery[SolutionsTable])(_.id)

  def * = (id, matchDuration, matchStartTime, interactorLog, participatingSolution1, participatingSolution2, winner) <> (MatchReport.tupled, MatchReport.unapply)
}