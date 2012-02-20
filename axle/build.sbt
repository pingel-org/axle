name := "axle"

version := "1.0"

organization := "org.pingel"

// scalaVersion := "2.9.1"
crossScalaVersions := Seq("2.9.1") // "2.8.1"

libraryDependencies ++= Seq(
  "jung" % "jung" % "1.7.6",
  // "org.jblas" % "jblas" % "1.2.0",
  "org.specs2" %% "specs2" % "1.8.1" % "test"
)
