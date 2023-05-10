package ru.letovo.gameset.logic

import ru.letovo.gameset.logic.Config.frame_root
import ru.letovo.gameset.web.models.Video

import java.io.File

class MultiPictureRenderer extends Renderer {
  private var callback: Video => Unit = _
  private var matchReport: MatchReport = _
  private var renderConfig: RenderConfig = _
  private var framesRoot: String = _

  override def startRendering(matchReport: MatchReport, renderConfig: RenderConfig, callback: Video => Unit): Unit = {
    this.callback = callback
    this.matchReport = matchReport
    this.renderConfig = renderConfig
    InvokerPool.addToQueue(new InvokerRequest(
      Array(prepareInvoker()),
      encode,
      None
    ))
  }

  def prepareInvoker(): Invoker = {
    val invoker = new Invoker(matchReport.interactorLogPath, Seq())
    framesRoot = frame_root + "/" + matchReport.matchId
    new File(framesRoot).mkdirs()

    invoker
  }

  def encode(): Unit = {
    callback(new Encoder().encode(framesRoot, matchReport.matchId, renderConfig))
  }
}
