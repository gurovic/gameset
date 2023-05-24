package ru.letovo.gameset.web.models


import models._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag



case class MatchReport(matchId: Long,
                       PlacesOnly: Boolean,
                       errorCode: Boolean,
                       interactorError: Boolean,
                       interactorLog: String)

case class SolutionScore(Id: Long,
                         solutionId: Long,
                         solutionScore: Int,
                         matchId: Long)


class MatchReportsTable(tag: Tag) extends Table[MatchReport](tag, "reports") {

  /** The ID column, which is the primary key, and auto incremented */
  def matchId = column[Long]("matchid", O.PrimaryKey, O.AutoInc)

  def placesOnly = column[Boolean]("placesonly")

  def errorCode = column[Boolean]("errorcode")
  def errorSolution = column[Boolean]("errorsolutions")

  def interactorError = column[Boolean]("interactorerror")

  def interactorLog = column[String]("interactorerrorlog")

  def * = (matchId, placesOnly, errorCode, errorSolution, interactorError, interactorLog)
}

 val reports = TableQuery[MatchReportsTable]

val solutions = TableQuery[SolutionsTable]
class  SolutionScoresTable(tag: Tag) extends Table[SolutionScore](tag, "matchreportresults") {
  def Id = column[Long]("Id", O.PrimaryKey, O.AutoInc)

  def solutionId = column[Long]("solutionId")

  def solutionScore = column[Int]("solutionScore")

  def matchId = column[Long]("matchId")

  def * = (Id, solutionId, solutionScore, matchId)

  def aFK = foreignKey("matchId", matchId, reports)(_.matchId, onDelete =
    ForeignKeyAction.Cascade)

  def bFK = foreignKey("solutionId", solutionId, solutions)(_.id, onDelete =
    ForeignKeyAction.Cascade)
}

