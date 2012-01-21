name := "category"

version := "2.0"

organization :=	"org.pingel"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "6.0.3",
  "org.pingel" %% "axle" % "1.0",
  "org.specs2" %% "specs2" % "1.7.1" % "test"
)

resolvers ++= Seq(
  "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots",
  "ibiblio" at "http://mirrors.ibiblio.org/pub/mirrors/maven2",
  "GlassFish Maven Repository" at "http://download.java.net/maven/glassfish",
  "ondex" at "http://ondex.rothamsted.bbsrc.ac.uk/nexus/content/groups/public",
  "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo"
)
