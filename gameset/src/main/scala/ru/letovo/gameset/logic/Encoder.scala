package ru.letovo.gameset.logic

import scala.sys.process.stringSeqToProcess

class Encoder() {
  def encode(sourcePath: String, targetPath: String, matchId: Int, config: RenderConfig): Video = {
    Seq("ffmpeg",
      "-r", config.framerate.toString,
      "-f image2",
      "-s", config.viewport.toString,
      "-i", sourcePath + "/%d.png",
      "-vcodec", "libx264",
      "-crf", "25",
      "-pix_fmt", "yuv420p",
      "-b:v", config.bitrate.toString,
      targetPath
    ).!

    new Video(RenderConfig, matchId, targetPath, System.currentTimeMillis())
  }
}
