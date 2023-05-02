package ru.letovo.gameset.web.models

import play.api.libs.json.{Json, OFormat}
import ru.letovo.gameset.logic.RenderConfig
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class Video(id: Long, path :String, renderedAt: Long, config: RenderConfig)

object Video {
  implicit val videoFormat: OFormat[Video] = Json.format[Video]
}

class VideosTable(tag: Tag) extends Table[Video](tag, "videos") {
  private val encodeFactor = 10000

  /**
   * This is the tables default "projection".
   *
   * It defines how the columns are converted to and from the Video object.
   */

  override def * = (id, path, renderedAt, (viewport, bitrate, framerate, compression)).shaped <> ( {
    case (id, path, renderedAt, renderConfig) =>
      Video(id, path, renderedAt, RenderConfig.tupled.apply(renderConfig))
  }, { v: Video =>
    def unpack(rc: RenderConfig) = RenderConfig.unapply(rc).get

    Some((v.id, v.path, v.renderedAt, unpack(v.config)))
  })

  /** The ID column, which is the primary key, and auto incremented */
  def id: Rep[Long] = column[Long]("id", O.PrimaryKey)

  def renderedAt: Rep[Long] = column[Long]("rendered_at")

  def viewport: Rep[Int] = column[Int]("viewport")

  def path: Rep[String] = column[String]("path")

  def bitrate: Rep[Float] = column[Float]("bitrate")

  def framerate: Rep[Float] = column[Float]("framerate")

  def compression: Rep[Int] = column[Int]("compression")
}

