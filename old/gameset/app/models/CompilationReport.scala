package models

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag
import models._

case class CompilationReport(
  id: Long,
  isSuccessful: Boolean,
  compilerMessage: String,
  path: String
)

class CompilationReportsTable(tag: Tag) extends Table[CompilationReport](tag, "CompilationReports") {
  def id = column[Long]("id", O.PrimaryKey)
  def isSuccessful = column[Boolean]("isSuccessful")
  def compilerMessage = column[String]("compilerMessage")
  def path = column[String]("path")
  def * = (id, isSuccessful, compilerMessage, path) <> (CompilationReport.tupled, CompilationReport.unapply) // possible error, TODO
}
