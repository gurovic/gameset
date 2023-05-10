package ru.letovo.gameset.logic

import ru.letovo.gameset.web.models.Video

import scala.sys.process.stringSeqToProcess

class Encoder() {
  def encode(sourcePath: String, matchId: Long, config: RenderConfig): Video = {
    Seq("ffmpeg",
      "-r", config.framerate.toString,
      "-f image2",
      "-s", config.viewport.toString,
      "-i", sourcePath + "/%d.png",
      "-vcodec", "libx264",
      "-crf", "25",
      "-pix_fmt", "yuv420p",
      "-b:v", config.bitrate.toString,
      Config.videos_root + s"/$matchId"
    ).!

    Video(matchId, System.currentTimeMillis(), config)
  }
}
