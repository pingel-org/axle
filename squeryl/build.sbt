name := "org.pingel.squeryl"

version := "1.0"

scalaVersion := "2.9.0-1"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.6",
  "org.squeryl" % "squeryl_2.9.0" % "0.9.4",
  "org.specs2" %% "specs2" % "1.5" % "test",
  "org.specs2" %% "specs2-scalaz-core" % "6.0.RC2" % "test"
)

resolvers ++= Seq(
  "ibiblio" at "http://mirrors.ibiblio.org/pub/mirrors/maven2",
  "eulergui" at "http://eulergui.sourceforge.net/maven2"
)
