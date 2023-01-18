package controllers

import play.api.mvc.{BaseController, ControllerComponents}

import javax.inject.Inject

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
