package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._

import models._
import java.util.Date
import anorm.{Id, NotAssigned}


object Blog extends Controller {

  /**
   * Contact Form definition.
   */
  val entryForm: Form[(String, String)] = Form(
    tuple(
      "title" -> nonEmptyText,
      "content" -> nonEmptyText
    )
  )

  val commentForm: Form[(String, String)] = Form(
    tuple(
      "text" -> nonEmptyText,
      "username" -> nonEmptyText
    )
  )
  
  /**
   * Display an empty form.
   */
  def newEntry = Action {
    Ok(html.blog.newEntry(entryForm));
  }

  def show(id:Long, title:String) = Action { implicit request =>
    BlogEntry.findById(id).map { entry =>
      Ok(html.blog.show(entry, commentForm, CommentEntry.all))
    }.getOrElse(NotFound)
  }
  
  def addComment(id:Long, title:String) = Action { implicit request =>
    
    BlogEntry.findById(id).map { entry =>
	    commentForm.bindFromRequest.fold(
	      commentForm_errors => BadRequest(html.blog.show(entry, commentForm_errors, CommentEntry.all)),
	      {
	        case (text, username) =>
	          val date = new Date()
	          val comment_entry =  CommentEntry.create(
	            CommentEntry(NotAssigned, text, username, date)
	          )
	          Ok(html.blog.show(entry, commentForm, CommentEntry.all))
	      }
	    )
    }.getOrElse(NotFound)
  }  

  def edit(id:Long) = Action {
    BlogEntry.findById(id).map { entry =>
      Ok(html.blog.edit(entry,entryForm.fill(entry.title,entry.content)))
    }.getOrElse(NotFound)
  }

  def update(id:Long) = Action {
    implicit request =>
      entryForm.bindFromRequest.fold(
        errors => BadRequest(html.blog.newEntry(errors)),
        {
          case (title, content) =>
            val date = new Date()
            val entry = BlogEntry.update(
              BlogEntry(Id(id), title, content, date)
              )
            Ok(html.blog.show(entry, commentForm, CommentEntry.all))
        }
        )
  }

  def create = Action { implicit request =>
    entryForm.bindFromRequest.fold(
      errors => BadRequest(html.blog.newEntry(errors)),
      {
        case (title, content) =>
          val date = new Date()
          val entry =  BlogEntry.create(
            BlogEntry(NotAssigned, title, content, date)
          )
          Ok(html.blog.show(entry, commentForm, CommentEntry.all))
        }
    )
  }

  /**
   * Display a form pre-filled with an existing Contact.
   */
  /*
  def editForm = Action {
    val existingContact = Contact(
      "Fake", "Contact", Some("Fake company"), informations = List(
        ContactInformation(
          "Personal", Some("fakecontact@gmail.com"), List("01.23.45.67.89", "98.76.54.32.10")
        ),
        ContactInformation(
          "Professional", Some("fakecontact@company.com"), List("01.23.45.67.89")
        ),
        ContactInformation(
          "Previous", Some("fakecontact@oldcompany.com"), List()
        )
      )
    )
    Ok(html.contact.form(contactForm.fill(existingContact)))
  }
*/
  /**
   * Handle form submission.
   */
  /*
  def submit = Action { implicit request =>
    contactForm.bindFromRequest.fold(
      errors => BadRequest(html.contact.form(errors)),
      contact => Ok(html.contact.summary(contact))
    )
  }
  */

}
