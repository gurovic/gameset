package ru.letovo.gameset.web.models


import models._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

case class MatchReport(matchId: Long,
                       solutionScores: Map[Long, Int],
                       placesOnly: Boolean,
                       errorCode: Boolean,
                       errorSolutionId: Long,
                       errorSolutionLog: String,
                       interactorError: Boolean,
                       interactorLog: String)



class MatchReportsTable(tag: Tag) extends Table[MatchReport](tag, "reports") {
  /** The ID column, which is the primary key, and auto incremented */
  def matchId = column[Long]("matchid", O.PrimaryKey, O.AutoInc)

  def placesOnly = column[Boolean]("placesonly")

  def errorCode = column[Boolean]("errorcode")
  def errorSolutionId = column[Long]("errorsolutionid")

  def errorSolutioLog = column[String]("errorsolutionlog")

  def interactorError = column[Boolean]("interactorerror")

  def interactorLog = column[String]("interactorerrorlog")

  def * = _
}


class MatchResutsTable extends Table[MatchReport](tag, "matchresults"){

  def * = _

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def matchId = column[Long]("matchid")

  def solutionId = column[Long]("solutionId")

  def solutionScore = column[Int]("solutionScore")

}

