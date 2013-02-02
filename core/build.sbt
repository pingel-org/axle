import AssemblyKeys._

name := "axle"

version := "0.1-SNAPSHOT"

// version := "0.1-M5"

organization := "org.pingel"

seq(assemblySettings: _*)

crossScalaVersions := Seq("2.9.1")

initialCommands in console := "import axle._; import axle.algebra._; import axle.stats._; import axle.quanta._; import axle.graph._; import axle.matrix._; import axle.ml._; import axle.visualize._; import axle.ast._; import collection._"

scalacOptions ++= Seq("-unchecked", "-deprecation")

// curl http://cloud.github.com/downloads/mikiobraun/jblas/jblas-1.2.0.jar -o lib/jblas-1.2.0.jar

libraryDependencies ++= Seq(
  "net.sf.jung" % "jung-algorithms" % "2.0.1",
  "net.sf.jung" % "jung-api" % "2.0.1",
  "net.sf.jung" % "jung-graph-impl" % "2.0.1",
  "net.sf.jung" % "jung-io" % "2.0.1",
  "net.sf.jung" % "jung-visualization" % "2.0.1",
  "org.specs2" %% "specs2" % "1.11" % "test",
  "joda-time" % "joda-time" % "2.1",
  "org.joda" % "joda-convert" % "1.2",
  "org.slf4j" % "slf4j-api" % "1.7.2"
  // "org.scalaz" %% "scalaz" % "6.0.4"
)

test in assembly := {}

// http://www.scala-sbt.org/using_sonatype.html

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) 
    Some("snapshots" at nexus + "content/repositories/snapshots") 
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://axle-lang.org</url>
  <licenses>
    <license>
      <name>BSD-2-Clause</name>
      <url>http://www.opensource.org/licenses/BSD-2-Clause</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:adampingel/axle.git</url>
    <connection>scm:git:git@github.com:adampingel/axle.git</connection>
  </scm>
  <developers>
    <developer>
      <id>pingel</id>
      <name>Adam Pingel</name>
      <url>http://pingel.org</url>
    </developer>
  </developers>)

// after 'publish', see:
//
// https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-8.ReleaseIt