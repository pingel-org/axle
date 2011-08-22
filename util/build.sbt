name := "org.pingel.util"

version := "1.0"

scalaVersion := "2.9.0-1"

libraryDependencies ++= Seq(
   "jung" % "jung" % "1.7.6",
   "att" % "grappa" % "1.2"
)

resolvers ++= Seq(
  "ibiblio" at "http://mirrors.ibiblio.org/pub/mirrors/maven2",
   "eulergui" at "http://eulergui.sourceforge.net/maven2"
)