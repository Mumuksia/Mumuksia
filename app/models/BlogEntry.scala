package models

import play.api.Play.current
import java.util.Date
import play.api.db.DB
import anorm._
import anorm.SqlParser._

case class BlogEntry(id: Pk[Long], title:String, content:String, date:Date)

object BlogEntry {

  val mapper = {
    get[Pk[Long]]("id") ~
    get[String]("title") ~
    get[String]("content") ~
    get[Date]("date") map {
      case id~title~content~date => BlogEntry(id, title, content, date)
    }
  }

  def findById(id: Long):Option[BlogEntry] = {
    DB.withConnection { implicit connection =>
      SQL("select * from entry where id = {id}").on('id -> id).as(BlogEntry.mapper.singleOpt)
    }
  }

  def all:Seq[BlogEntry] = {
    DB.withConnection { implicit connection =>
      SQL("select * from entry order by date DESC").as(BlogEntry.mapper *)
    }
  }

  def create(entry:BlogEntry):BlogEntry = {

    DB.withConnection { implicit connection =>

      val id: Long = entry.id.getOrElse {
        SQL("select next value for entry_seq").as(scalar[Long].single)
      }

      SQL("insert into entry values ({id}, {title}, {content}, {date})").on(
        'id -> id,
        'title -> entry.title,
        'content -> entry.content,
        'date -> entry.date
      ).executeUpdate()

      entry.copy(id = Id(id))

    }
  }

  def update(entry:BlogEntry):BlogEntry = {

    DB.withConnection { implicit connection =>

      SQL("update entry set title={title}, content={content}, date={date} where id = {id}").on(
        'title -> entry.title,
        'content -> entry.content,
        'date -> entry.date,
        'id -> entry.id
      ).executeUpdate()
     entry
    }
  }

}
