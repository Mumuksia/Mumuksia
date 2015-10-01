name := "Mumuksia"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  cache
)

libraryDependencies += "com.typesafe.play" %% "anorm" % "2.4.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

