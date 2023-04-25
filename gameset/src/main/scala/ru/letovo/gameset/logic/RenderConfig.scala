package ru.letovo.gameset.logic

import ru.letovo.gameset.logic.VideoCodec.VideoCodec

case class RenderConfig(viewport: ViewportSize, bitrate: Float /*mbps*/ , framerate: Float, codec: VideoCodec, compression: CompressionValue)
