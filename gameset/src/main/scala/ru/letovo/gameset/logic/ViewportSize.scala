package ru.letovo.gameset.logic

case class ViewportSize(width: Int, height: Int){
  override def toString: String = width.toString + "x" + height.toString
}
