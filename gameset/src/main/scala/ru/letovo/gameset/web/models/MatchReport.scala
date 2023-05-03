package ru.letovo.gameset.web.models


import models._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class MatchReport(matchId: Long,
                       solutionScores: Map[Long, Int],
                       PlacesOnly: Boolean,
                       errorCode: Boolean,
                       errorSolution: Map[Long,String],
                       interactorError: Boolean,
                       interactorLog: String)



class MatchReportsTable(tag: Tag) extends Table[MatchReport](tag, "reports") {


  def * = (matchId, solutionScores, placesOnly, errorCode, errorSolution) <> (MatchReport.tupled, MatchReport.unapply)

  /** The ID column, which is the primary key, and auto incremented */
  def matchId = column[Long]("matchid")

  def solutionScores =  column[Map[Long, Int]]("solutionscores")
  def placesOnly = column[Boolean]("placesonly")

  def errorCode = column[Boolean]("errorcode")
  def errorSolution = column[Map[Long, String]]("errorsolutions")

  def interactorError = column[Boolean]("interactorerror")

  def interactorLog = column[String]("interactorerrorlog")

}
