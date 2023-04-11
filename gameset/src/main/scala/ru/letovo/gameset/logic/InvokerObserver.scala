package ru.letovo.gameset.logic

trait InvokerObserver {
  def recieve(request_id: String): Unit

}
