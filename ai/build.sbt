name := "ai"

version := "2.0"

organization := "org.pingel"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.pingel" %% "axle" % "1.0",
  "org.slf4j" % "slf4j-simple" % "1.6.1",
  "org.slf4j" % "slf4j-api" % "1.6.1",
  "org.specs2" %% "specs2" % "1.7.1" % "test"
)
