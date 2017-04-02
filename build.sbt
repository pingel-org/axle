import com.typesafe.sbt.pgp.PgpKeys.publishSigned
import com.typesafe.sbt.SbtSite.SiteKeys._
import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import sbtunidoc.Plugin.UnidocKeys._
import ReleaseTransformations._
// import ScoverageSbtPlugin._

lazy val spireVersion = "0.14.1"
lazy val shapelessVersion = "2.3.2"
lazy val catsVersion = "0.9.0" // must match spire's algebra's catsVersion
lazy val disciplineVersion = "0.7.2"
lazy val scalaCheckVersion = "1.13.4"
lazy val scalaTestVersion = "3.0.0"

lazy val scalaXmlVersion = "1.0.5"
lazy val scalaParserCombinatorsVersion = "1.0.4"
lazy val sparkVersion = "2.0.1"
lazy val jungVersion = "2.1"
lazy val jblasVersion = "1.2.4"
lazy val jacksonVersion = "2.8.4"
lazy val jodaTimeVersion = "2.9.4"
lazy val jodaConvertVersion = "1.8.1"
lazy val jogampVersion = "2.3.2"
lazy val akkaVersion = "2.4.17"

lazy val scoverageSettings = Seq(
  coverageMinimum := 10,
  coverageFailOnMinimum := false,
  coverageHighlighting := true
)

lazy val buildSettings = Seq(
  organization := "org.axle-lang",
  scalaVersion := "2.11.8",
  scalaOrganization := "org.typelevel",
  crossScalaVersions := Seq("2.11.8")
)

lazy val axleDoctestSettings = Seq(
  doctestWithDependencies := false
)

lazy val axleCore = Project(
    id = "axle-core",
    base = file("axle-core"))
  .settings(
    name := "axle-core",
    moduleName := "axle-core",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % scalaTestVersion, // TODO % "test",
      "org.typelevel" %% "discipline" % disciplineVersion,
      "org.typelevel" %% "spire" % spireVersion,
      "org.typelevel" %% "spire-laws" % spireVersion,
      "org.typelevel" %% "cats-core" % catsVersion,
      "eu.timepit" %% "singleton-ops" % "0.0.4",
      "org.scala-lang.modules" %% "scala-xml" % scalaXmlVersion,
      "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion,
      "org.scalacheck" %% "scalacheck" % scalaCheckVersion % "test"
  ))
  .settings(axleSettings:_*)
  .settings(commonJvmSettings:_*)

lazy val publishSettings = Seq(
  homepage := Some(url("http://axle-lang.org")),
  licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0")),
  scmInfo := Some(ScmInfo(url("https://github.com/axlelang/axle"), "scm:git:git@github.com:axlelang/axle.git")),
  autoAPIMappings := true,
  apiURL := Some(url("http://axle-lang.org/axle/api/")),
  publishArtifact in (Compile, packageDoc) := {
    CrossVersion.partialVersion(scalaVersion.value) match {
      // case Some((2, 10)) => false  // don't package scaladoc when publishing for 2.10
      case _ => true
    }
  },
  pomExtra := (
  <developers>
    <developer>
      <id>pingel</id>
      <name>Adam Pingel</name>
      <url>https://github.com/adampingel</url>
    </developer>
  </developers>)
) ++ credentialSettings ++ sharedPublishSettings ++ sharedReleaseProcess

lazy val commonSettings = Seq(
  scalacOptions ++= commonScalacOptions,
  resolvers ++= Seq(
    "Concurrent Maven Repo" at "http://conjars.org/repo",
    // "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases", // needed transitively by specs2
    "bintray/non" at "http://dl.bintray.com/non/maven",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    // TODO simulacrum, machinist, etc
  ),
  parallelExecution in Test := false
//  scalacOptions in (Compile, doc) := (scalacOptions in (Compile, doc)).value.filter(_ != "-Xfatal-warnings")
) ++ warnUnusedImport

lazy val tagName = Def.setting{
 s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}"
}

lazy val commonJvmSettings = Seq(
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
) ++ axleDoctestSettings

lazy val axleSettings = buildSettings ++ commonSettings ++ publishSettings ++ scoverageSettings

lazy val disciplineDependencies = Seq(
  libraryDependencies += "org.scalacheck" %% "scalacheck" % scalaCheckVersion,
  libraryDependencies += "org.typelevel" %% "discipline" % disciplineVersion
)

lazy val axleAlgorithms = Project(
  id = "axle-algorithms",
  base = file("axle-algorithms"),
  settings = axleSettings
).settings(
  name := "axle-algorithms",
  libraryDependencies ++= Seq(
    "com.chuusai" %% "shapeless" % shapelessVersion
  )
).dependsOn(axleCore)

lazy val axleLanguages = Project(
  id = "axle-languages",
  base = file("axle-languages"),
  settings = axleSettings
).settings(
  name := "axle-languages",
  libraryDependencies ++= Seq(
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion
  )
).dependsOn(axleCore)

lazy val axleGames = Project(
  id = "axle-games",
  base = file("axle-games"),
  settings = axleSettings
).settings(
  name := "axle-games",
  libraryDependencies ++= Seq()
).dependsOn(axleCore)

lazy val axleJoda = Project(
  id = "axle-joda",
  base = file("axle-joda"),
  settings = axleSettings
).settings(
  name := "axle-joda",
  libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % jodaTimeVersion % "provided",
    "org.joda" % "joda-convert" % jodaConvertVersion % "provided"
  )
).dependsOn(axleCore)

lazy val axleJblas = Project(
  id = "axle-jblas",
  base = file("axle-jblas"),
  settings = axleSettings
).settings(
  name := "axle-jblas",
  libraryDependencies ++= Seq(
    "org.jblas" % "jblas" % jblasVersion % "provided"
  )
).dependsOn(axleCore)

lazy val axleJung = Project(
  id = "axle-jung",
  base = file("axle-jung"),
  settings = axleSettings
).settings(
  name := "axle-jung",
  libraryDependencies ++= Seq(
    "net.sf.jung" % "jung-algorithms" % jungVersion % "provided",
    "net.sf.jung" % "jung-api" % jungVersion % "provided",
    "net.sf.jung" % "jung-graph-impl" % jungVersion % "provided",
    "net.sf.jung" % "jung-io" % jungVersion % "provided"
  )
).dependsOn(axleCore)

lazy val axleSpark = Project(
  id = "axle-spark",
  base = file("axle-spark"),
  settings = axleSettings
).settings(
  name := "axle-spark",
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
  )
).dependsOn(axleCore)

lazy val axleVisualize = Project(
  id = "axle-visualize",
  base = file("axle-visualize"),
  settings = axleSettings
).settings(
  name := "axle-visualize",
  libraryDependencies ++= Seq(
    "net.sf.jung" % "jung-visualization" % jungVersion % "provided",
    "net.sf.jung" % "jung-algorithms" % jungVersion % "provided",
    "net.sf.jung" % "jung-api" % jungVersion % "provided",
    "net.sf.jung" % "jung-graph-impl" % jungVersion % "provided",
    "net.sf.jung" % "jung-io" % jungVersion % "provided",
    "joda-time" % "joda-time" % jodaTimeVersion % "provided",
    "org.joda" % "joda-convert" % jodaConvertVersion % "provided",
    "org.jblas" % "jblas" % jblasVersion % "provided",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion % "provided",
    "com.typesafe.akka" %% "akka-stream" % akkaVersion % "provided",
    "org.jogamp.gluegen" % "gluegen-rt-main" % jogampVersion % "provided", // other jogl deps: http://jogamp.org/wiki/index.php/Maven
    "org.jogamp.jogl" % "jogl-all-main" % jogampVersion % "provided"
  )
).dependsOn(axleCore, axleJung, axleAlgorithms, axleJoda)

lazy val axleTest = Project(
  id = "axle-test",
  base = file("axle-test"),
  settings = axleSettings
).settings(
  name := "axle-test",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "org.jblas" % "jblas" % jblasVersion,
    "joda-time" % "joda-time" % jodaTimeVersion,
    "org.joda" % "joda-convert" % jodaConvertVersion,
    "net.sf.jung" % "jung-visualization" % jungVersion,
    "net.sf.jung" % "jung-algorithms" % jungVersion,
    "net.sf.jung" % "jung-api" % jungVersion,
    "net.sf.jung" % "jung-graph-impl" % jungVersion,
    "net.sf.jung" % "jung-io" % jungVersion,
    "org.jogamp.gluegen" % "gluegen-rt-main" % jogampVersion,
    "org.jogamp.jogl" % "jogl-all-main" % jogampVersion
  )
).dependsOn(
  axleCore,
  axleAlgorithms,
  axleVisualize,
  axleJoda,
  axleJblas,
  axleJung,
  axleGames,
  axleLanguages
)

lazy val docSettings = Seq(
  autoAPIMappings := true,
  unidocProjectFilter in (ScalaUnidoc, unidoc) :=
    inProjects(axleCore),  // -- inProjects(noDocProjects(scalaVersion.value): _*),
  site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "api"),
  site.addMappingsToSiteDir(tut, "_tut"),
  ghpagesNoJekyll := false,
  siteMappings += file("CONTRIBUTING.md") -> "contributing.md",
  scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
//    "-Xfatal-warnings",
    "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath,
    "-diagrams"
  ),
  git.remoteRepo := "git@github.com:axlelang/axle.git",
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md"
)

lazy val docs = Project(
    id = "axle-docs",
    base = file("axle-docs"))
  .settings(moduleName := "axle-docs")
  .settings(axleSettings)
  .settings(noPublishSettings)
  .settings(unidocSettings)
  .settings(site.settings)
  .settings(ghpages.settings)
  .settings(docSettings)
  .settings(tutSettings)
  .settings(tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))))
  .settings(commonJvmSettings)
  .dependsOn(axleTest) // was only "core"

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val commonScalacOptions = Seq(
// "-optimize",
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
//  "-language:existentials",
  "-language:higherKinds",
//  "-language:implicitConversions",
//  "-language:experimental.macros",
  "-language:postfixOps",
  "-unchecked",
//  "-Xfatal-warnings",
  "-Xlint",
  "-Yliteral-types",
//  "-Yinline-warnings",
  "-Yno-adapted-args",
//  "-Ywarn-dead-code",
//  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

// http://www.scala-sbt.org/using_sonatype.html

lazy val sharedPublishSettings = Seq(
  releaseCrossBuild := true,
  releaseTagName := tagName.value,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := Function.const(false),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("Snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
  }
)

lazy val sharedReleaseProcess = Seq(
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    //runClean, // disabled to reduce memory usage during release
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
    pushChanges)
)

lazy val warnUnusedImport = Seq(
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 10)) =>
        Seq()
      case Some((2, n)) if n >= 11 =>
        Seq("-Ywarn-unused-import")
    }
  },
  scalacOptions in (Compile, console) ~= {_.filterNot("-Ywarn-unused-import" == _)},
  scalacOptions in (Test, console) <<= (scalacOptions in (Compile, console))
)
lazy val credentialSettings = Seq(
  // For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)
