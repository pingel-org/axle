name := "axle"

version := "1.0"

organization := "org.pingel"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "jung" % "jung" % "1.7.6",
  "org.specs2" %% "specs2" % "1.7.1" % "test"
)

//resolvers ++= Seq(
//"Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots",
//"ibiblio" at "http://mirrors.ibiblio.org/pub/mirrors/maven2",
//"GlassFish Maven Repository" at "http://download.java.net/maven/glassfish"
//)
