package ru.letovo.gameset.logic

import ru.letovo.gameset.logic.RenderConfig
import ru.letovo.gameset.web.models.Video

trait Renderer {
  def startRendering(matchReport: MatchReport, renderConfig: RenderConfig, callback: Video => Unit): Unit
}
