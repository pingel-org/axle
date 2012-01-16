name := "org.pingel.ai"

version := "2.0"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.scalaz" % "scalaz-core_2.9.1" % "6.0.3",
  "org.slf4j" % "slf4j-simple" % "1.6.1",
  "org.slf4j" % "slf4j-api" % "1.6.1"
  // "org.specs2" % "specs2" %% "1.5" % "test"
  // "org.specs2" % "specs2-scalaz-core" % "6.0.3" % "test"
)

resolvers ++= Seq(
  "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots",
  "ibiblio" at "http://mirrors.ibiblio.org/pub/mirrors/maven2",
  "GlassFish Maven Repository" at "http://download.java.net/maven/glassfish",
  "ondex" at "http://ondex.rothamsted.bbsrc.ac.uk/nexus/content/groups/public",
  "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo"
)
