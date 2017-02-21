package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.twirl.api.Html
import services.file.FileService
import services.github.GithubService
import utils.Application

@javax.inject.Singleton
class GithubController @javax.inject.Inject() (override val app: Application, githubService: GithubService) extends BaseController {
  def test() = act(s"project.test") { implicit request =>
    githubService.test().map { result =>
      Ok(views.html.github.test(result))
    }
  }

  def detail(key: String) = act(s"project.$key") { implicit request =>
    githubService.detail(key).map {
      case Some(repo) => Ok(views.html.github.detail(repo))
      case None => Ok(Html(s"Github repo for [$key] not found."))
    }
  }

  def create(key: String) = act(s"project.create.$key") { implicit request =>
    githubService.create("scala-js-" + key).map { result =>
      Redirect(controllers.routes.GithubController.detail(key))
    }
  }
}
