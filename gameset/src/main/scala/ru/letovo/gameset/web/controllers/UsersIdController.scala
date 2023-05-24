package ru.letovo.gameset.web.controllers

import play.api.mvc._
import ru.letovo.gameset.web.models.{User, UsersTable}
import slick.jdbc.PostgresProfile.api._
import slick.lifted

import java.nio.file.{Files, Paths}
import javax.inject._
import scala.annotation.unused
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

@Singleton
class UsersIdController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  import slick.jdbc.PostgresProfile.api.Database
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  val db = Database.forConfig("postgres")
  val usersTable = lifted.TableQuery[UsersTable]

  def users_id(userID: Long) = Action { implicit request =>
    val userFuture = db.run {
      usersTable.filter(_.id === userID).result
    }

    userFuture.onComplete {
      case Success(user) => Ok(views.html.users_id(userID, user))
      case Failure(e) =>
        println("No user")
        e.printStackTrace()
        Redirect(routes.HomeController.index()).flashing("error" -> e.toString)
    }
    Ok("ok")
  }
}