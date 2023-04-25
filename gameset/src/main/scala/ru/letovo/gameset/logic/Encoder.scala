package ru.letovo.gameset.logic

import ru.letovo.gameset.logic.VideoCodec.VideoCodec

import scala.sys.process.stringSeqToProcess

class Encoder(videoCodec: VideoCodec) {
  def encode(path: String): Video = {
    Seq("ffmpeg", "-r", ).!
  }
}
