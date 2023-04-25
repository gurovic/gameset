package ru.letovo.gameset.logic

import ru.letovo.gameset.logic.VideoCodec.VideoCodec

case class RenderConfig(viewport: ViewportSize, bitrate: Float /*mbps*/ , codec: VideoCodec, compression: Int) {
  require(compression >= 0 && compression <= 100, "Compression value must be between 0 and 100")
}
