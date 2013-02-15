name := "axle-scalding"

version := "0.1-SNAPSHOT"

organization := "org.pingel"

scalaVersion := "2.10.0"

crossScalaVersions := Seq("2.10.0")

initialCommands in console := "import axle._; import axle.algebra._; import axle.stats._; import axle.quanta._; import axle.graph._; import axle.matrix._; import axle.ml._; import axle.visualize._; import axle.ast._; import collection._"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers += "conjars" at "http://conjars.org/repo/"

libraryDependencies ++= Seq(
  "org.pingel" %% "axle" % "0.1-SNAPSHOT",
  "com.twitter" %% "scalding" % "0.8.3-SNAPSHOT",
  "org.specs2" %% "specs2" % "1.11" % "test"
)

