name := "org.pingel.ai"

version := "2.0"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
//  "org.pingel.util" %% "org.pingel.util" % "2.0",
//  "org.pingel.util" % "org.pingel.util" % "1.1.0",
  "org.scalaz" % "scalaz-core_2.8.1" % "6.0-SNAPSHOT",
  "org.slf4j" % "slf4j-simple" % "1.6.1",
  "org.slf4j" % "slf4j-api" % "1.6.1",
  "org.specs2" % "specs2_2.9.0" % "1.5" % "test",
  "org.specs2" % "specs2-scalaz-core_2.9.0" % "6.0.RC2" % "test",
  "org.scalala" % "scalala_2.9.0" % "1.0.0.RC2-SNAPSHOT"
)

resolvers ++= Seq(
  "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots",
  "ibiblio" at "http://mirrors.ibiblio.org/pub/mirrors/maven2",
  "GlassFish Maven Repository" at "http://download.java.net/maven/glassfish",
  "ondex" at "http://ondex.rothamsted.bbsrc.ac.uk/nexus/content/groups/public",
  "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo"
)
