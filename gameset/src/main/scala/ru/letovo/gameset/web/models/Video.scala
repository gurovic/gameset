package ru.letovo.gameset.web.models

import play.api.libs.json.Json
import ru.letovo.gameset.logic.RenderConfig
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

case class Video(id: Long, renderedAt: Long, config: RenderConfig)

object Video {
  implicit val videoFormat = Json.format[Video]
}

class VideosTable(tag: Tag) extends Table[Video](tag, "videos") {

  /**
   * This is the tables default "projection".
   *
   * It defines how the columns are converted to and from the Video object.
   */

  override def * = (id,  renderedAt, (viewport, bitrate, compression)).shaped <> ( {
    case (id,  renderedAt, renderConfig) =>
      Video(id,  renderedAt, RenderConfig.tupled.apply(renderConfig))
  }, { v: Video =>
    def unpack(rc: RenderConfig) = RenderConfig.unapply(rc).get

    Some((v.id, v.renderedAt, unpack(v.config)))
  })

  /** The ID column, which is the primary key, and auto incremented */
  def id: Rep[Long] = column[Long]("id", O.PrimaryKey)

  def matchID: Rep[Long] = column[Long]("match_id")

  def renderedAt: Rep[Long] = column[Long]("rendered_at")

  def viewport: Rep[Int] = column[Int]("viewport")

  def bitrate: Rep[Float] = column[Float]("bitrate")

  def compression: Rep[Int] = column[Int]("compression")
}

