
package axle

import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._
import org.scalastyle.sbt.ScalastylePlugin
import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Utilities._
import com.typesafe.sbt.pgp.PgpKeys
import com.typesafe.sbt.pgp.PgpKeys._

import scoverage.ScoverageSbtPlugin._

object AxleBuild extends Build {

  lazy val scoverageSettings = Seq(
    ScoverageKeys.coverageMinimum := 10,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )

  val sharedSettings = Project.defaultSettings ++ releaseSettings ++ scoverageSettings ++ Seq(

    organization := "org.axle-lang",

    scalaVersion := "2.11.7",

    //crossScalaVersions := Seq("2.10.4", "2.11.7"),

    crossScalaVersions := Seq("2.11.7"),

    libraryDependencies ++= Seq(
      "org.typelevel" %% "discipline" % "0.2.1",
      "org.specs2" %% "specs2" % "2.4.17" % "test",
      "org.scala-lang.modules" %% "scala-xml" % "1.0.4",
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
    ),

    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature",
      "-optimize",
      "-Yinline-warnings",
      "-language:higherKinds",
      "-language:postfixOps"),

    initialCommands in console := """
//import axle._
""",

    // http://www.scala-sbt.org/using_sonatype.html
    publishMavenStyle := true,

    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT")) 
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },

    publishArtifact in Test := false,

    pomIncludeRepository := { _ => false },

    resolvers ++= Seq(
      "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/",
      "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases", // needed transitively by specs2
      "Concurrent Maven Repo" at "http://conjars.org/repo"
    ),

    pomExtra := (
  <url>http://axle-lang.org</url>
  <licenses>
    <license>
      <name>Apache-2.0</name>
      <url>http://opensource.org/licenses/Apache-2.0</url>
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
      <url>https://github.com/adampingel</url>
    </developer>
  </developers>)

  ) ++ ScalastylePlugin.Settings

  lazy val publishSignedArtifacts = ReleaseStep(
    action = st => {
      val extracted = st.extract
      val ref = extracted.get(thisProjectRef)
      extracted.runAggregated(publishSigned in Global in ref, st)
    },
    check = st => {
      // getPublishTo fails if no publish repository is set up.
      val ex = st.extract
      val ref = ex.get(thisProjectRef)
      Classpaths.getPublishTo(ex.get(publishTo in Global in ref))
      st
    },
    enableCrossBuild = true
  )

  releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishSignedArtifacts,
      setNextVersion,
      commitNextVersion,
      pushChanges
    )

  lazy val axleCore = Project(
    id = "axle-core",
    base = file("axle-core"),
    settings = sharedSettings
  ).settings(
    name := "axle-core",
    libraryDependencies ++= Seq(
      "org.spire-math" %% "spire" % "0.10.1",
      "org.spire-math" %% "spire-scalacheck-binding" % "0.10.1"
    )
  )

  lazy val axleAlgorithms = Project(
    id = "axle-algorithms",
    base = file("axle-algorithms"),
    settings = sharedSettings
  ).settings(
    name := "axle-algorithms",
    libraryDependencies ++= Seq(
      "com.chuusai" %% "shapeless" % "2.2.2"
    )
  ).dependsOn(axleCore)

  lazy val axleLanguages = Project(
    id = "axle-languages",
    base = file("axle-languages"),
    settings = sharedSettings
  ).settings(
    name := "axle-languages",
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.3"
    )
  ).dependsOn(axleCore)

  lazy val axleGames = Project(
    id = "axle-games",
    base = file("axle-games"),
    settings = sharedSettings
  ).settings(
    name := "axle-games",
    libraryDependencies ++= Seq()
  ).dependsOn(axleCore)

  lazy val axleJoda = Project(
    id = "axle-joda",
    base = file("axle-joda"),
    settings = sharedSettings
  ).settings(
    name := "axle-joda",
    libraryDependencies ++= Seq(
      "joda-time" % "joda-time" % "2.3",
      "org.joda" % "joda-convert" % "1.6"
    )
  ).dependsOn(axleCore)

  lazy val axleJblas = Project(
    id = "axle-jblas",
    base = file("axle-jblas"),
    settings = sharedSettings
  ).settings(
    name := "axle-jblas",
    libraryDependencies ++= Seq(
      "org.jblas" % "jblas" % "1.2.3"
    )
  ).dependsOn(axleCore)
/*
  lazy val axleJcublas = Project(
    id = "axle-jcublas",
    base = file("axle-jcublas"),
    settings = sharedSettings
  ).settings(
    name := "axle-jcublas",
    libraryDependencies ++= Seq(
      "org.nd4j" % "jcublas" % "6.5"
    )
  ).dependsOn(axleCore)
*/
/*
  lazy val axleMtj = Project(
    id = "axle-mtj",
    base = file("axle-mtj"),
    settings = sharedSettings
  ).settings(
    name := "axle-mtj",
    libraryDependencies ++= Seq(
      "com.github.fommil.netlib" % "all" % "1.1.2" pomOnly(),
      "com.googlecode.matrix-toolkits-java" % "mtj" % "1.0.1"
    )
  ).dependsOn(axleCore)
*/
  lazy val jungVersion = "2.0.1"

  lazy val axleJung = Project(
    id = "axle-jung",
    base = file("axle-jung"),
    settings = sharedSettings
  ).settings(
    name := "axle-jung",
    libraryDependencies ++= Seq(
      "net.sf.jung" % "jung-algorithms" % jungVersion,
      "net.sf.jung" % "jung-api" % jungVersion,
      "net.sf.jung" % "jung-graph-impl" % jungVersion,
      "net.sf.jung" % "jung-io" % jungVersion
    )
  ).dependsOn(axleCore)

  lazy val hadoopVersion = "1.1.2"

/*
  lazy val axleHBase = Project(
    id = "axle-hbase",
    base = file("axle-hbase"),
    settings = sharedSettings
  ).settings(
    name := "axle-hbase",
    libraryDependencies ++= Seq(
      "org.apache.hadoop" % "hadoop-core" % hadoopVersion,
      "org.apache.hbase" % "hbase" % "0.94.7"
    )
  ).dependsOn(axleCore)
*/
  lazy val axleSpark = Project(
    id = "axle-spark",
    base = file("axle-spark"),
    settings = sharedSettings
  ).settings(
    name := "axle-spark",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "1.5.0" % "provided"
    )
  ).dependsOn(axleCore)

/*
  lazy val axleScalding = Project(
    id = "axle-scalding",
    base = file("axle-scalding"),
    settings = sharedSettings
  ).settings(
    name := "axle-scalding",
    libraryDependencies ++= Seq(
      "org.apache.hadoop" % "hadoop-core" % hadoopVersion,
      "com.twitter" %% "scalding-core" % "0.13.1"
    )
  ).dependsOn(axleCore)
*/
/*
  lazy val axleFigaro = Project(
    id = "axle-figaro",
    base = file("axle-figaro"),
    settings = sharedSettings
  ).settings(
    name := "axle-figaro",
    libraryDependencies ++= Seq(
      "com.cra.figaro" %% "figaro" % "3.0.0.0"
    )
  ).dependsOn(axleCore)
*/
  lazy val axleVisualize = Project(
    id = "axle-visualize",
    base = file("axle-visualize"),
    settings = sharedSettings
  ).settings(
    name := "axle-visualize",
    libraryDependencies ++= Seq(
      "net.sf.jung" % "jung-visualization" % jungVersion,
      "com.typesafe.akka" %% "akka-actor" % "2.3.3",
      "org.jogamp.gluegen" % "gluegen-rt-main" % "2.0.2", // other jogl deps: http://jogamp.org/wiki/index.php/Maven
      "org.jogamp.jogl" % "jogl-all-main" % "2.0.2"
    )
  ).dependsOn(axleCore, axleJung, axleAlgorithms, axleJoda)

  lazy val axleTest = Project(
    id = "axle-test",
    base = file("axle-test"),
    settings = sharedSettings
  ).settings(
    name := "axle-test",
    libraryDependencies ++= Seq(
    )
  ).dependsOn(
    axleCore,
    axleAlgorithms,
    axleVisualize,
    axleJoda,
    axleJblas,
    //axleJcublas,
    axleJung,
    axleGames,
    axleLanguages
    //axleFigaro
    )

  lazy val axleAggregate = Project(
    id = "axle-aggregate",
    base = file("axle-aggregate"),
    settings = sharedSettings
  ).settings(
    name := "axle-aggregate",
    test := { },
    publish := { },
    publishLocal := { }
  ).aggregate(
    axleCore,
    axleAlgorithms,
    axleGames,
    axleLanguages,
    axleJblas,
    //axleJcublas,
    axleJung,
    axleJoda,
    axleSpark,
    //axleScalding,
    //axleHBase,
    //axleFigaro,
    axleVisualize)
/*
  lazy val axleRepl = Project(
    id = "axle-repl",
    base = file("axle-repl"),
    settings = sharedSettings ++ assemblySettings
  ).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*).settings(

    name := "axle-repl",

    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-compiler" % "2.11.7",
      "jline" % "jline" % "2.11" // http://grokbase.com/t/gg/scala-internals/1433gjdg5h/missing-jline-for-scala-2-11-0-rc1
    ),

    mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) => {
      case PathList("org", "fusesource", xs @ _*) => MergeStrategy.first
      case PathList("META-INF", "native", xs @ _*) => MergeStrategy.first
      case PathList("scala", "reflect", "api", xs @ _*) => MergeStrategy.first
      case PathList("libnativewindow_x11.so") => MergeStrategy.first   // jogl
      case PathList("libnewt.so") => MergeStrategy.first               // jogl
      case PathList("newt.dll") => MergeStrategy.first                 // jogl
      case PathList("libgluegen-rt.so") => MergeStrategy.first         // jogl
      case PathList("nativewindow_awt.dll") => MergeStrategy.first
      case PathList("nativewindow_win32.dll") => MergeStrategy.first
      case PathList("libnativewindow_awt.so") => MergeStrategy.first
      case PathList("libjogl_mobile.so") => MergeStrategy.first        // jogl
      case PathList("jogl_mobile.dll") => MergeStrategy.first
      case PathList("jogl_desktop.dll") => MergeStrategy.first
      case PathList("libjogl_desktop.so") => MergeStrategy.first
      case PathList("gluegen-rt.dll") => MergeStrategy.first
      case x => old(x)
    }
  }

  ).dependsOn(axleCore, axleVisualize, axleGames, axleLanguages)
*/
}