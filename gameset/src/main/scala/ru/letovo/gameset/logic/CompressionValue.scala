package ru.letovo.gameset.logic

case class CompressionValue(value: Int) {
  require(value >= 0 && value <= 100, "Compression value must be between 0 and 100")
}
