package ru.letovo.gameset.web.models


import models._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class MatchReport(matchId: Long,
                       solutionScores: Map[Long, Int],
                       PlacesOnly: Boolean,
                       ErrorCode: Boolean,
                       ErrorSolutions: List[Int])



class MatchReportsTable(tag: Tag) extends Table[MatchReport](tag, "reports") {


  def * = (matchId, solutionScores, PlacesOnly, ErrorCode, ErrorSolutions) <> (MatchReport.tupled, MatchReport.unapply)

  /** The ID column, which is the primary key, and auto incremented */
  def matchId = column[Long]("matchId")

  def solutionScores =  column[Map[Long, Int]]("solutionScores")
  def PlacesOnly = column[Boolean]("PlacesOnly")

  def ErrorCode = column[Boolean]("ErrorCode")
  def ErrorSolutions = column[List[Int]]("ErrorSolutions")

}

