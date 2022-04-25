# Installation

Axle as a dependency of an SBT project.

## Install SBT

See [SBT](http://www.scala-sbt.org/)

## Create SBT Project

```bash
mkdir demo
cd demo
```

Create a `build.sbt` file

```sbt
name := "demo"

version := "0.1-SNAPSHOT"

organization := "org.acme"

scalaVersion := "2.13.3"

resolvers += "sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "org.axle-lang" %% "axle-core"  % "@RELEASE_VERSION@",
  "org.axle-lang" %% "axle-xml"   % "@RELEASE_VERSION@",
  "org.axle-lang" %% "axle-jung"  % "@RELEASE_VERSION@",
  "org.axle-lang" %% "axle-jblas" % "@RELEASE_VERSION@",
  "org.axle-lang" %% "axle-joda"  % "@RELEASE_VERSION@"
)
```

(Less commonly used `axle-laws`, `axle-awt`, and `axle-parallel` are not shown.)

The Axle jars are compiled with several additional dependencies in `provided` scope,
meaning that they are compiled and packaged with the expectation that the user of the Axle
jars will explicitly provide those dependencies.

As of version 0.5.2 the full list of dependencies is below.
Add this section to your `build.sbt` file to pull them all in to the `demo` project:

```sbt
libraryDependencies ++= Seq(

  // needed by axle-jung (and for unit conversions)
  "net.sf.jung"            % "jung-visualization" % "2.1",
  "net.sf.jung"            % "jung-algorithms"    % "2.1",
  "net.sf.jung"            % "jung-api"           % "2.1",
  "net.sf.jung"            % "jung-graph-impl"    % "2.1",
  //"net.sf.jung"          % "jung-io"            % "2.1",

  // for animations
  "io.monix"               %% "monix-reactive"    % "2.3.0",
  "io.monix"               %% "monix-cats"        % "2.3.0",

  // needed by axle-jblas
  "org.jblas"              % "jblas"              % "1.2.4",

  // needed by axle-joda
  "joda-time"              % "joda-time"          % "2.9.4",
  "org.joda"               % "joda-convert"       % "1.8.1",

  // needed by axle-xml
  "org.scala-lang.modules" %% "scala-xml"         % "1.3.0",
)
```

## Next Steps

Run `sbt console` to launch the Scala REPL with the Axle jars in the classpath.
Axle works well interactively -- especially during prototyping, debugging, and testing.
Any of the [Axle tutorials](/tutorial/) can be copied and pasted into the REPL.

To start writing code, do `mkdir -p src/main/scala/org/acme/demo`, and add your code there.

## Releases

@RELEASE_VERSION@ is the most recent released version:

See the [Road Map](../appendix/RoadMap.md) for more information on the release schedule.

## Snapshots

Snapshot versions are created for every commit and
hosted on the [Sonatype snapshot repo](https://oss.sonatype.org/content/repositories/snapshots).
