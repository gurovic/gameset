package ru.letovo.gameset.logic

object Config {
  val files_storage_root: String = System.getProperty("user.dir")
  val exe_test_files_root: String = System.getProperty("user.dir") + "/data/test"
  val example_game_root: String = System.getProperty("user.dir") + "/data/test/example_game"
  val videos_root: String = System.getProperty("user.dir") + "/data/videos"
  val frame_root: String = System.getProperty("user.dir") + "/data/frames"
}
