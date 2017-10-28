import com.typesafe.sbt.pgp.PgpKeys.publishSigned
import com.typesafe.sbt.SbtSite.SiteKeys._
import ReleaseTransformations._
// import ScoverageSbtPlugin._

// TODO site, tut, unidoc, doctestwithdependencies
// TODO scoverage, noPublishSettings, release plugin
// TODO minimum sbt version

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
lazy val monixVersion = "2.3.0"
lazy val jogampVersion = "2.3.2"

lazy val scoverageSettings = Seq(
  coverageMinimum := 10,
  coverageFailOnMinimum := false,
  coverageHighlighting := true
)

//scalaVersion := "2.12.3"
scalaVersion := "2.11.8"

lazy val buildSettings = Seq(
  organization := "org.axle-lang",
  scalaVersion := "2.11.8",
  scalaOrganization := "org.typelevel",
  crossScalaVersions := Seq("2.11.8")
)

lazy val axleDoctestSettings = Seq(
  // doctestWithDependencies := false
)

lazy val axleCore = Project("axle-core", file("axle-core"))
  .settings(
    name := "axle-core",
    moduleName := "axle-core",
    libraryDependencies ++= Seq(
      "org.typelevel"          %% "spire"                    % spireVersion,
      "org.typelevel"          %% "spire-laws"               % spireVersion,
      "org.typelevel"          %% "cats-core"                % catsVersion,
      "org.typelevel"          %% "cats-free"                % catsVersion,
      "com.chuusai"            %% "shapeless"                % shapelessVersion,
      // "eu.timepit"          %% "singleton-ops"            % "0.0.4"
      "io.monix"               %% "monix-reactive"           % monixVersion,
      "io.monix"               %% "monix-cats"               % monixVersion,
      "org.scala-lang.modules" %% "scala-xml"                % scalaXmlVersion,
      "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion,
      "org.scalatest"          %% "scalatest"                % scalaTestVersion, // TODO % "test",
      "org.scalacheck"         %% "scalacheck"               % scalaCheckVersion  % "test",
      "org.typelevel"          %% "discipline"               % disciplineVersion,
      // needed for visualizations
      "net.sf.jung"            %  "jung-visualization"       % jungVersion        % "provided",
      "net.sf.jung"            %  "jung-algorithms"          % jungVersion        % "provided",
      "net.sf.jung"            %  "jung-api"                 % jungVersion        % "provided",
      "net.sf.jung"            %  "jung-graph-impl"          % jungVersion        % "provided",
      // "net.sf.jung"            %  "jung-io"                  % jungVersion        % "provided",
      // other jogl deps: http://jogamp.org/wiki/index.php/Maven
      "org.jogamp.gluegen"     %  "gluegen-rt-main"          % jogampVersion      % "provided",
      "org.jogamp.jogl"        %  "jogl-all-main"            % jogampVersion      % "provided",
      // something references this.  would be nice to remove:
      "com.google.code.findbugs" % "jsr305"                  % "3.0.0"            % "provided"
  ))
  .settings(axleSettings)
  .settings(commonJvmSettings)

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
)

lazy val tagName = Def.setting{
 s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}"
}

lazy val commonJvmSettings = Seq(
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
) ++ axleDoctestSettings

lazy val axleSettings = buildSettings ++ commonSettings ++ publishSettings ++ scoverageSettings

lazy val axleJoda = Project("axle-joda", file("axle-joda"))
 .settings(axleSettings)
 .settings(
  name := "axle-joda",
  libraryDependencies ++= Seq(
    "joda-time"                % "joda-time"                 % jodaTimeVersion    % "provided",
    "org.joda"                 % "joda-convert"              % jodaConvertVersion % "provided"
  )
).dependsOn(axleCore)

lazy val axleJblas = Project("axle-jblas", file("axle-jblas"))
 .settings(axleSettings)
 .settings(
   name := "axle-jblas",
   libraryDependencies ++= Seq(
     "org.jblas"              % "jblas"                     % jblasVersion       % "provided"
   )
).dependsOn(axleCore)

lazy val axleSpark = Project("axle-spark", file("axle-spark"))
 .settings(axleSettings)
 .settings(
  name := "axle-spark",
  libraryDependencies ++= Seq(
    "org.apache.spark"         %% "spark-core"               % sparkVersion       % "provided"
  )
).dependsOn(axleCore)

lazy val axleWheel = Project("axle-wheel", file("axle-wheel"))
 .settings(axleSettings)
 .settings(
  name := "axle-wheel",
  libraryDependencies ++= Seq(
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
    "org.jblas"                    %  "jblas"                % jblasVersion,
    "joda-time"                    %  "joda-time"            % jodaTimeVersion,
    "org.joda"                     %  "joda-convert"         % jodaConvertVersion,
    "net.sf.jung"                  %  "jung-visualization"   % jungVersion,
    "net.sf.jung"                  %  "jung-algorithms"      % jungVersion,
    "net.sf.jung"                  %  "jung-api"             % jungVersion,
    "net.sf.jung"                  %  "jung-graph-impl"      % jungVersion,
    // "net.sf.jung"                  %  "jung-io"              % jungVersion,
    "org.jogamp.gluegen"           %  "gluegen-rt-main"      % jogampVersion,
    "org.jogamp.jogl"              %  "jogl-all-main"        % jogampVersion
  )
).dependsOn(
  axleCore,
  axleJoda,
  axleJblas
)

lazy val docs = Project("axle-docs", file("axle-docs"))
  .settings(moduleName := "axle-docs")
  .settings(axleSettings)
  //.settings(noPublishSettings)
  //.settings(site.settings)
  .enablePlugins(TutPlugin)
  //.settings(site.addMappingsToSiteDir(tut, "tut"))
  .settings(
    autoAPIMappings := true,
    git.remoteRepo := "git@github.com:axlelang/axle.git",
    includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md"
  )
  .settings(commonJvmSettings)
  .dependsOn(axleWheel)

//lazy val noPublishSettings = Seq(
//  publish := (),
//  publishLocal := (),
//  publishArtifact := false
//)

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
  "-Xfatal-warnings",
  "-Xlint",
//  "-Yliteral-types",
//  "-Yinline-warnings",
  "-Ywarn-unused-import",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
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
    //ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
    pushChanges)
)

lazy val credentialSettings = Seq(
  // For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)
