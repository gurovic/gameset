package models
import slick.jdbc.PostgresProfile.api._

case class Program(id: Long) // TODO

class ProgramsTable(tag: Tag) extends Table[Program](tag, "programs") {
  def id = column[Long]("id", O.PrimaryKey)

  def * = id <> (Program.apply, Program.unapply)
}
