ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "final-final-final"
  )
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"


libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor-typed_2.13" % "2.6.14",
  "com.typesafe.akka" % "akka-stream-typed_2.13" % "2.6.14",
  "com.typesafe.akka" % "akka-http_2.13" % "10.2.4",
  "com.typesafe.akka" % "akka-http-spray-json_2.13" % "10.2.4"
)
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC7"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test