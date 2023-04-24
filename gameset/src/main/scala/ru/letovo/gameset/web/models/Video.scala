package ru.letovo.gameset.web.models

import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.Json
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

case class Video(id: Long, matchID: Long, renderedAt: Long) {
  def path = s"../data/games/$matchID/videos/$id"
}

object Video {
  implicit val videoFormat = Json.format[Video]
}

class VideosTable(tag: Tag) extends Table[Video](tag, "videos") {

  /**
   * This is the tables default "projection".
   *
   * It defines how the columns are converted to and from the Video object.
   *
   * In this case, we are simply passing the ID, matchID and renderedAt parameters to the Video case classes
   * apply and unapply methods.
   */
  def * = (ID, matchID, renderedAt) <> ((Video.apply _).tupled, Video.unapply)

  /** The ID column, which is the primary key, and auto incremented */
  def ID = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def matchID = column[Long]("match_id")

  def renderedAt = column[Long]("rendered_at")

}

/**
 * A repository for videos.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class VideosRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.

  import dbConfig._
  import profile.api._

  /**
   * The starting point for all queries on the videos table.
   */
  private val video = TableQuery[VideosTable]

  /**
   * Create a video with the given matchID and renderedAt.
   *
   * This is an asynchronous operation, it will return a future of the created video, which can be used to obtain the
   * id for that video.
   */
  def create(matchID: Long, renderedAt: Long): Future[Video] = db.run {
    // We create a projection of just the matchID and renderedAt columns, since we're not inserting a value for the id column
    (video.map(v => (v.matchID, v.renderedAt))
      // Now define it to return the id, because we want to know what id was generated for the match
      returning video.map(_.ID)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((matchIDRenderedAt, id) => Video(id, matchIDRenderedAt._1, matchIDRenderedAt._2))
      // And finally, insert the video into the database
      ) += (matchID, renderedAt)
  }

  def list(): Future[Seq[Video]] = db.run {
    video.result
  }

  def getVideo(id: Long) = db.run {
    video.filter(_.ID === id).result
  }
}
