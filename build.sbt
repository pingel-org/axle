
lazy val spireVersion = "0.17.0-M1"
lazy val shapelessVersion = "2.4.0-M1"
lazy val kittensVersion = "2.1.0"
lazy val catsVersion = "2.2.0" // match spire's algebra's catsVersion (and monix)
lazy val catsEffectVersion = "2.1.4"

lazy val disciplineVersion = "1.0.0"
lazy val scalaCheckVersion = "1.14.1"
lazy val scalaTestVersion = "3.2.5"

lazy val scalaXmlVersion = "1.3.0"
lazy val scalaParserCombinatorsVersion = "1.1.2"
lazy val scalaParallelCollectionsVersion = "0.2.0"
lazy val jungVersion = "2.1"
lazy val jblasVersion = "1.2.5"
lazy val jacksonVersion = "2.11.2"
lazy val jodaTimeVersion = "2.9.4"
lazy val jodaConvertVersion = "1.8.1"
lazy val monixVersion = "3.1.0"
lazy val jogampVersion = "2.3.2"

lazy val scoverageSettings = Seq(
  coverageMinimumStmtTotal := 10,
  coverageFailOnMinimum := false,
  coverageHighlighting := true
)

scalaVersion := "2.13.3"

ThisBuild / crossScalaVersions := Seq("2.13.3")

publish / skip := true

autoCompilerPlugins := true

lazy val buildSettings = Seq(
  organization := "org.axle-lang",
  scalaVersion := "2.13.3",
  crossScalaVersions := Seq("2.13.3")
)

lazy val axleDoctestSettings = Seq(
  // doctestWithDependencies := false
)

lazy val axleCore = Project("axle-core", file("axle-core"))
  .settings(
    name := "axle-core",
    moduleName := "axle-core",
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "org.typelevel"          %% "spire"                    % spireVersion,
      "org.typelevel"          %% "spire-laws"               % spireVersion,
      "org.typelevel"          %% "cats-core"                % catsVersion,
      "org.typelevel"          %% "cats-effect"              % catsEffectVersion,
      "org.typelevel"          %% "cats-free"                % catsVersion,
      "com.chuusai"            %% "shapeless"                % shapelessVersion,
      "org.typelevel"          %% "kittens"                  % kittensVersion,
      // "eu.timepit"          %% "singleton-ops"            % "0.0.4"
      "io.monix"               %% "monix-reactive"           % monixVersion,
      "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion,
      "org.scalatest"          %% "scalatest"                % scalaTestVersion, // TODO % "test",
      "org.scalatest"          %% "scalatest-funsuite"       % scalaTestVersion, // TODO % "test",
      "org.scalacheck"         %% "scalacheck"               % scalaCheckVersion  % "test",
      "org.typelevel"          %% "discipline-core"          % disciplineVersion,
      // needed for visualizations
      "net.sf.jung"            %  "jung-visualization"       % jungVersion        % "provided",
      "net.sf.jung"            %  "jung-algorithms"          % jungVersion        % "provided",
      "net.sf.jung"            %  "jung-api"                 % jungVersion        % "provided",
      "net.sf.jung"            %  "jung-graph-impl"          % jungVersion        % "provided",
      // "net.sf.jung"            %  "jung-io"                  % jungVersion        % "provided"
  ))
  .settings(axleSettings)
  .settings(commonJvmSettings)

lazy val publishSettings = Seq(
    organization := "org.axle-lang",
    homepage := Some(url("https://axle-lang.org")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
    Developer(
      "adampingel",
      "Adam Pingel",
      "adam@axle-lang.org",
      url("https://github.com/adampingel")
    ))
)

lazy val commonSettings = Seq(
  scalacOptions ++= commonScalacOptions,
  resolvers ++= Seq(
    "Concurrent Maven Repo" at "https://conjars.org/repo",
    "bintray/non" at "https://dl.bintray.com/non/maven",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  parallelExecution in Test := false
)

lazy val commonJvmSettings = Seq(
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
) ++ axleDoctestSettings

lazy val axleSettings = buildSettings ++ commonSettings ++ publishSettings ++ scoverageSettings

lazy val axleAwt = Project("axle-awt", file("axle-awt"))
 .settings(axleSettings)
 .settings(
  name := "axle-awt",
  libraryDependencies ++= Seq(
    // jung is needed for unitted quantities
    "net.sf.jung"         %  "jung-visualization"    % jungVersion        % "provided",
    "net.sf.jung"         %  "jung-algorithms"       % jungVersion        % "provided",
    "net.sf.jung"         %  "jung-api"              % jungVersion        % "provided",
    "net.sf.jung"         %  "jung-graph-impl"       % jungVersion        % "provided"
  )
).dependsOn(axleCore)

lazy val axleParallel = Project("axle-parallel", file("axle-parallel"))
 .settings(axleSettings)
 .settings(
  name := "axle-parallel",
  libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-parallel-collections" % scalaParallelCollectionsVersion
  )
).dependsOn(axleCore)

lazy val axleXml = Project("axle-xml", file("axle-xml"))
 .settings(axleSettings)
 .settings(
  name := "axle-xml",
  libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-xml"          % scalaXmlVersion,
    // for visualizations (until jung is pulled out of axle-core)
    "net.sf.jung"            %  "jung-visualization"       % jungVersion        % "provided",
    "net.sf.jung"            %  "jung-algorithms"          % jungVersion        % "provided",
    "net.sf.jung"            %  "jung-api"                 % jungVersion        % "provided",
    "net.sf.jung"            %  "jung-graph-impl"          % jungVersion        % "provided",
  )
).dependsOn(axleCore)

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

lazy val axleLaws = Project("axle-laws", file("axle-laws"))
 .settings(axleSettings)
 .settings(
  name := "axle-laws",
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-verbosity", "3"),
  libraryDependencies ++= Seq()
).dependsOn(axleCore)

lazy val axleWheel = Project("axle-wheel", file("axle-wheel"))
 .settings(axleSettings)
 .settings(
  name := "axle-wheel",
  publish / skip := true,
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-verbosity", "3"),
  libraryDependencies ++= Seq(
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
    "org.jblas"                    %  "jblas"                % jblasVersion,
    "joda-time"                    %  "joda-time"            % jodaTimeVersion,
    "org.joda"                     %  "joda-convert"         % jodaConvertVersion,
    "net.sf.jung"                  %  "jung-visualization"   % jungVersion,
    "net.sf.jung"                  %  "jung-algorithms"      % jungVersion,
    "net.sf.jung"                  %  "jung-api"             % jungVersion,
    "net.sf.jung"                  %  "jung-graph-impl"      % jungVersion
    // "net.sf.jung"                  %  "jung-io"              % jungVersion
  )
).dependsOn(
  axleCore,
  axleLaws,
  axleAwt,
  axleXml,
  axleParallel,
  axleJoda,
  axleJblas
)

lazy val docwd = "docwork"

lazy val docs = Project("axle-docs", file("axle-docs"))
  .enablePlugins(MdocPlugin, LaikaPlugin, SitePlugin, GhpagesPlugin)
  .settings(axleSettings)
  .settings(
    moduleName := "axle-docs",
    autoAPIMappings := true,
    publish / skip := true,
    mdocVariables := Map(
      "DOCWD" -> docwd,
      "RELEASE_VERSION" -> (ThisBuild / previousStableVersion).value.getOrElse("0.6.3")
    ),
    mdocIn := file("axle-docs/src/main/mdoc"),
    mdocOut := file("axle-docs/target/mdoc"),
    siteSourceDirectory := file("axle-docs/target/site"),
    makeSite / includeFilter := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.svg" | "*.js" | "*.swf" | "*.yml" | "*.md" | "favicon.ico" | "*.woff" | "*.woff2" | "*.pdf",
    Laika / sourceDirectories := Seq(
      file("axle-docs/target/mdoc"),
      file("axle-docs/src/site"),
      file(docwd)
    ),
    laikaSite / target := file("axle-docs/target/site"),
    laikaExtensions ++= Seq(
      laika.markdown.github.GitHubFlavor,
      laika.parse.code.SyntaxHighlighting),
      laikaIncludePDF := true,
    ghpagesNoJekyll := true,
    ghpagesCleanSite / excludeFilter :=
      new FileFilter {
        def accept(f: File) = (ghpagesRepository.value / "CNAME").getCanonicalPath == f.getCanonicalPath
      } || "versions.html",
    git.remoteRepo := "git@github.com:axlelang/axle.git"
  )
  .settings(commonJvmSettings)
  .dependsOn(axleWheel)

lazy val commonScalacOptions = Seq(
// "-optimize",
  "-deprecation",
  "-encoding", "UTF-8",
  "-target:jvm-1.8",
  "-feature",
//  "-language:existentials",
  "-language:higherKinds",
//  "-language:implicitConversions",
//  "-language:experimental.macros",
  "-language:postfixOps",
  "-unchecked",
//  "-Xfatal-warnings",
  "-Xlint",
//  "-Yliteral-types",
//  "-Yinline-warnings",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

sonatypeProfileName := "org.axle-lang"
