ThisBuild / organization := "ru.letovo.gameset"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .settings(
    name := "web",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-slick" % "5.0.0",
      jdbc,
      guice,
      "org.postgresql" % "postgresql" % "42.5.1",
      "com.google.inject" % "guice" % "5.1.0",
      "com.google.inject.extensions" % "guice-assistedinject" % "5.1.0",
    ),
    routesGenerator := InjectedRoutesGenerator
  )


libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % "test"
