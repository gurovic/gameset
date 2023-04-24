package ru.letovo.gameset.web.models

import org.mindrot.jbcrypt.BCrypt
import slick.jdbc.PostgresProfile.api._

object PasswordUtils {
  def getPasswordHash(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  def checkPassword(password: String, hash: String): Boolean = {
    BCrypt.checkpw(password, hash)
  }
}

final case class User(id: Int, username: String, passwordHash: String, rating: Int) {
  def checkPassword(password: String): Boolean = {
    PasswordUtils.checkPassword(password, passwordHash)
  }
}

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Int]("id")
  def username = column[String]("username")
  def passwordHash = column[String]("passwordHash")
  def rating = column[Int]("rating")
  def * = (id, username, passwordHash, rating) <> ((User.apply _).tupled, User.unapply)
}
