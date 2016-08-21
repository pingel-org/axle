---
layout: page
title: Installation
permalink: /chapter/installation/
---

Axle as a dependency of an SBT project.

Usage
-----

1. Install [sbt](https://github.com/harrah/xsbt/wiki/Getting-Started-Setup)
2. Create a simple sbt project directory structure

```bash
mkdir demo
cd demo
```
3. Create a `build.sbt` file

```
name := "demo"

version := "0.1-SNAPSHOT"

organization := "org.acme"

scalaVersion := "2.11.7"

resolvers += "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += "sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "org.axle-lang" %% "axle-core" % "releaseVersion",
  "org.axle-lang" %% "axle-games" % "releaseVersion",
  "org.axle-lang" %% "axle-visualize" % "releaseVersion",
  "org.axle-lang" %% "axle-jblas" % "releaseVersion",
  "org.axle-lang" %% "axle-joda" % "releaseVersion",
  "org.axle-lang" %% "axle-jung" % "releaseVersion"
)

initialCommands in console := """
import axle._
// ...
"""
```

4. Run `sbt console` to launch the Scala REPL with the Axle jars in the classpath.
Axle works well interactively -- especially during prototyping, debugging, and testing.

5. To start writing code, do `mkdir -p src/main/scala/org/acme/demo`, and add your code there.

Releases
--------

Version snapshotVersion is hosted on the Sonatype snapshot repo (https://oss.sonatype.org/content/repositories/snapshots).
The schedule for publishing snapshot versions is irregular.
The full dependency is:

```
"org.axle-lang" %% "axle-core" % "snapshotVersion"
```

releaseVersion is the most recent released version:

```
"org.axle-lang" %% "axle-core" % "releaseVersion"
```

In addition to `axle-core` the artifacts `axle-algorithms`, `axle-games`,
`axle-visualize`, `axle-jblas`, `axle-joda`, and `axle-jung`
are also published to Sonatype.

See the [Road Map](/road_map/) for more information on the release schedule.
