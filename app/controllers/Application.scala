package controllers

import play.api._
import play.api.mvc._

import views._
import models.BlogEntry

object Application extends Controller {
  
  def index = Action {
    Ok(html.index(BlogEntry.all));
  }
  
}