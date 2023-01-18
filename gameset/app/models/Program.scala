package models
import slick.jdbc.PostgresProfile.api._

case class Program(id: Int) // TODO

class ProgramsTable(tag: Tag) extends Table[Program](tag, "programs") {
  def id = column[Int]("id", O.PrimaryKey)

  def * = id <> (Program.apply, Program.unapply)
}

val Programs = TableQuery[ProgramsTable]