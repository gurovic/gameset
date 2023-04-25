addSbtPlugin(Defaults.sbtPluginExtra(m = "com.typesafe.play" % "sbt-plugin" % "2.8.19", sbtV = "1.8.2", scalaV = "2.13.10"))

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
