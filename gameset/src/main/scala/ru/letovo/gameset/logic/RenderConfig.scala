package ru.letovo.gameset.logic

case class RenderConfig(viewport: Int, bitrate: Float /*mbps*/ , framerate: Float /*fps*/ , compression: Int) {
  require(compression >= 0 && compression <= 100, "Compression value must be between 0 and 100")
}
