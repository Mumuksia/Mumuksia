package models

import play.api.Play.current
import java.util.Date
import play.api.db.DB
import anorm._
import anorm.SqlParser._


case class CommentEntry(id: Pk[Long], text:String, username:String, date:Date)

object CommentEntry {

  val mapper = {
    get[Pk[Long]]("id") ~
    get[String]("text") ~
    get[String]("username") ~
    get[Date]("date") map {
      case id~text~username~date => CommentEntry(id, text, username, date)
    }
  }

  def findById(id: Long):Option[CommentEntry] = {
    DB.withConnection { implicit connection =>
      SQL("select * from comment where id = {id}").on('id -> id).as(CommentEntry.mapper.singleOpt)
    }
  }

  def all:Seq[CommentEntry] = {
    DB.withConnection { implicit connection =>
      SQL("select * from comment order by date DESC").as(CommentEntry.mapper *)
    }
  }

  def create(entry:CommentEntry):CommentEntry = {

    DB.withConnection { implicit connection =>

      val id: Long = entry.id.getOrElse {
        SQL("select next value for comment_seq").as(scalar[Long].single)
      }

      SQL("insert into comment values ({id}, {text}, {username}, {date})").on(
        'id -> id,
        'text -> entry.text,
        'username -> entry.username,
        'date -> entry.date
      ).executeUpdate()

      entry.copy(id = Id(id))

    }
  }
}
