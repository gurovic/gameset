package ru.letovo.gameset.web.models

import slick.jdbc.PostgresProfile.api._
import slick.lifted
import slick.lifted.Tag

import scala.concurrent.Future

case class Solution(id: Option[Long], tournamentID: Long, creatorID: Long, name: String) {

  /**
   * Author's solutions uses (-1) ID
   */

  def path = s"../data/games/$tournamentID/user-solutions/${id.getOrElse(-10L)}"

  def isAuthorSolution: Boolean = id.getOrElse(0L) == -1L
}

class SolutionsTable(tag: Tag) extends Table[Solution](tag, "solutions") {

  /**
   * This is the tables default "projection".
   *
   * It defines how the columns are converted to and from the Solution object.
   */

  def * = (id.?, tournamentID, creatorID, name) <> ((Solution.apply _).tupled, Solution.unapply)

  /** The ID column, which is the primary key and auto incremented */
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def tournamentID = column[Long]("tournament_id")

  def creatorID = column[Long]("creator_id")

  def name = column[String]("name")
}


class SolutionsRepository(db: Database) {

  val solutionsTable = lifted.TableQuery[SolutionsTable]

  def newAuthorSolution(tournamentID: Long, name: String): Future[Solution] = {
    val newSolution = (solutionsTable returning solutionsTable.map(_.id)
      into ((user, id) => user.copy(id = Some(id)))
      ) += Solution(Some(-1), tournamentID, -1, name)

    db.run(newSolution)
  }

  def newUserSolution(tournamentID: Long, creatorID: Long, name: String): Future[Solution] = {
    val newSolution = (solutionsTable returning solutionsTable.map(_.id)
      into ((user, id) => user.copy(id = Some(id)))
      ) += Solution(None, tournamentID, creatorID, name)

    db.run(newSolution)
  }

  def findAllByID(id: Long) = {
    db.run {
      solutionsTable.filter(_.id === id).result
    }
  }

  def findAllByTournamentID(tournamentID: Long) = {
    db.run {
      solutionsTable.filter(_.tournamentID === tournamentID).result
    }
  }

  def deleteByID(id: Long): Future[Int] = {
    db.run {
      solutionsTable.filter(_.id === id).delete
    }
  }
}