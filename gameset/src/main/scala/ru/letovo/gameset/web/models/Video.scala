package ru.letovo.gameset.web.models

import ru.letovo.gameset.logic.{Config, RenderConfig, ViewportSize}
import slick.jdbc.PostgresProfile.api._
import slick.lifted
import slick.lifted.Tag

import scala.concurrent.Future

case class Video(id: Long, renderedAt: Long, config: RenderConfig) {
  def path: String = Config.videos_root + s"/$id"
}

object Video {
}

class VideosTable(tag: Tag) extends Table[Video](tag, "videos") {
  private val encodeFactor = 10000

  /**
   * This is the tables default "projection".
   *
   * It defines how the columns are converted to and from the Video object.
   */

  override def * = (id, renderedAt, viewport, (bitrate, framerate, compression)).shaped <> ( {
    case (id, renderedAt, viewport, renderConfig) =>
      Video(id, renderedAt, RenderConfig(decodeViewPort(viewport), renderConfig._1, renderConfig._2, renderConfig._3))
  }, { v: Video =>
    def unpackWOPort(rc: RenderConfig) = {
      val configTuple = RenderConfig.unapply(rc)
      (configTuple.get._2, configTuple.get._3, configTuple.get._4)
    }

    Some((v.id, v.renderedAt, encodeViewPort(v.config.viewport), unpackWOPort(v.config)))
  })

  /** The ID column, which is the primary key, and auto incremented */
  def id: Rep[Long] = column[Long]("id", O.PrimaryKey)

  def decodeViewPort(encoded: Int): ViewportSize = {
    ViewportSize(encoded / encodeFactor, encoded % encodeFactor)
  }

  def encodeViewPort(viewportSize: ViewportSize): Int = {
    viewportSize.height * encodeFactor + viewportSize.width
  }

  def renderedAt: Rep[Long] = column[Long]("rendered_at")

  def viewport: Rep[Int] = column[Int]("viewport")

  def bitrate: Rep[Float] = column[Float]("bitrate")

  def framerate: Rep[Float] = column[Float]("framerate")

  def compression: Rep[Int] = column[Int]("compression")
}

class VideoDAO(db: Database) {

  val videoTable = lifted.TableQuery[VideosTable]

  def save(video: Video): Future[Video] = db.run {
    videoTable returning videoTable += video
  }

  def findByID(id: Long): Future[Video] = db.run {
    videoTable.filter(_.id === id).result.head
  }
}
