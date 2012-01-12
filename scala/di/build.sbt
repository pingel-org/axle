name := "org.pingel.di"

version := "1.0"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "6.0.3",
  "org.specs2" %% "specs2" % "1.6.1" % "test",
  "org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test"
)

resolvers ++= Seq(
  "ibiblio" at "http://mirrors.ibiblio.org/pub/mirrors/maven2",
  "eulergui" at "http://eulergui.sourceforge.net/maven2"
)
