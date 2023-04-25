package ru.letovo.gameset.web.models

import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

object PasswordUtils {
  def getPasswordHash(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  def checkPassword(password: String, hash: String): Boolean = {
    BCrypt.checkpw(password, hash)
  }
}

final case class User(id: Long, username: String, passwordHash: String, rating: Int) {
  def checkPassword(password: String): Boolean = {
    PasswordUtils.checkPassword(password, passwordHash)
  }
}

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("id")
  def username = column[String]("username")
  def passwordHash = column[String]("passwordHash")
  def rating = column[Int]("rating", O.Default(0))
  def * = (id, username, passwordHash, rating) <> ((User.apply _).tupled, User.unapply)
}

@Singleton
class UsersRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]


  import dbConfig._
  import profile.api._

  private val users = TableQuery[Users]

  def create(username: String, password: String): Future[User] = db.run {
    (users.map(user => (user.username, user.passwordHash))
      returning users.map(_.id)
      into ((result, id) => User(id, result._1, result._2, 0))
      ) += (username, PasswordUtils.getPasswordHash(password))
  }

  def list(): Future[Seq[User]] = db.run {
    users.result
  }

  def getUserByID(id: Long) = db.run {
    users.filter(_.id === id).result.head
  }

  def getUserByUsername(username: String) = db.run {
    users.filter(_.username === username).result.head
  }
}