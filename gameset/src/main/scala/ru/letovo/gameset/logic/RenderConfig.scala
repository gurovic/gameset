package ru.letovo.gameset.logic

import VideoCodec.VideoCodec

case class RenderConfig(viewport: ViewportSize, bitrate: Float /*mbps*/ , codec: VideoCodec, compression: CompressionValue)
