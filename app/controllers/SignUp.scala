package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._

import models._

object SignUp extends Controller {
  
  /**
   * Sign Up Form definition.
   *
   * Once defined it handle automatically, ,
   * validation, submission, errors, redisplaying, ...
   */
  val signupForm: Form[User] = Form(
    
    // Define a mapping that will handle User values
    mapping(
      "email" -> email,
      
      "password" -> tuple(
        "main" -> text(minLength = 6),
        "confirm" -> text
      ).verifying(
        // Add an additional constraint: both passwords must match
        "Passwords don't match", passwords => passwords._1 == passwords._2
      ),

      "accept" -> checked("You must accept the conditions")
      
    )
    {
      // Binding: Create a User from the mapping result (ignore the second password and the accept field)
      (email, passwords, _) => User(email, passwords._1)
    }
    {
      // Unbinding: Create the mapping values from an existing User value
      user => Some(user.email, (user.password, ""), false)
    }
  )
  
  /**
   * Display an empty form.
   */
  def form = Action {
    Ok(html.signup.form(signupForm));
  }

  /**
   * Handle form submission.
   */
  def submit = Action { implicit request =>
    signupForm.bindFromRequest.fold(
      // Form has errors, redisplay it
      errors => BadRequest(html.signup.form(errors)),
      
      // We got a valid User value, display the summary
      user => Redirect(routes.Application.index)
    )
  }
  
}