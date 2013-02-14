name := "axle-scalding"

version := "0.1-SNAPSHOT"

organization := "org.pingel"

crossScalaVersions := Seq("2.9.1")

initialCommands in console := "import axle._; import axle.algebra._; import axle.stats._; import axle.quanta._; import axle.graph._; import axle.matrix._; import axle.ml._; import axle.visualize._; import axle.ast._; import collection._"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers += "conjars" at "http://conjars.org/repo/"

libraryDependencies ++= Seq(
  "org.pingel" %% "axle" % "0.1-SNAPSHOT",
  "com.twitter" % "scalding_2.9.2" % "0.8.2",
  // "com.twitter" % "algebird-core_2.9.2" % "0.1.8",
  "org.specs2" %% "specs2" % "1.11" % "test"
)

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
