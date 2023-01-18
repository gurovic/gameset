package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class GamesController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def index() = Action {
    Ok("TODO") // #24 @seawind777
  }

  def addGet() = Action {

    Ok("Todo")
  }

  def addPost() = Action { req =>
    Ok("Todo")
  }
}
