package ru.letovo.gameset.logic

import ru.letovo.gameset.web.models.Video

class MultiPictureRenderer extends Renderer {
  override def startRendering(matchReport: MatchReport, renderConfig: RenderConfig, callback: Video => Unit): Unit = {
    InvokerPool.addToQueue(new InvokerRequest(
      Array(prepareInvoker(matchReport)),
      encode,
      None
    ))
  }

  def prepareInvoker(matchReport: MatchReport): Invoker = {
    new Invoker(matchReport.interactorLogPath, Seq())
  }

  def encode(): Unit = {
//    callback(new Encoder().encode(, matchReport.matchId, renderConfig))
  }
}
