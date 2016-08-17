
package axle.site.chapter

import axle.site._
import axle.blog.model._
import axle.site.Version.releaseVersion
import axle.site.Version.snapshotVersion

object Installation {

  def sbt = Resource("sbt", "https://github.com/harrah/xsbt/wiki/Getting-Started-Setup")

  val chapter = Chapter(
    "Installation",
    <div>
Axle as a dependency of an SBT project.
    </div>,
    Section("Usage",
      <div>
        <p>1. Install { sbt.link }</p>
        <p>2. Create a simple sbt project directory structure</p>
      </div>,
      CodeSnippet("""
mkdir demo
cd demo
""", "bash"),
      <span>3. Create a <code>build.sbt</code> file</span>,
      CodeSnippet(s"""
name := "demo"

version := "0.1-SNAPSHOT"

organization := "org.acme"

scalaVersion := "2.11.7"

resolvers += "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += "sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "org.axle-lang" %% "axle-core" % "${releaseVersion}",
  "org.axle-lang" %% "axle-games" % "${releaseVersion}",
  "org.axle-lang" %% "axle-visualize" % "${releaseVersion}",
  "org.axle-lang" %% "axle-jblas" % "${releaseVersion}",
  "org.axle-lang" %% "axle-joda" % "${releaseVersion}",
  "org.axle-lang" %% "axle-jung" % "${releaseVersion}"
)

initialCommands in console := """ + "\"\"\"" + """
import axle._
// ...
""" + "\"\"\""),
      <p>
        4. Run<code>sbt console</code>
        to launch the Scala REPL with the Axle jars in the classpath.  Axle works well interactively -- especially during prototyping, debugging, and testing.
      </p>,
      <p>
        5. To start writing code, do<code>mkdir -p src/main/scala/org/acme/demo</code>
        , and add your code there.
      </p>),
    Section("Releases",
      <div>
Version ${snapshotVersion} is hosted on the Sonatype snapshot repo (https://oss.sonatype.org/content/repositories/snapshots).
The schedule for publishing snapshot versions is irregular.
The full dependency is:
<pre>
  "org.axle-lang" %% "axle-core" % "${snapshotVersion}"
</pre>
${releaseVersion} is the most recent released version:
<pre>
  "org.axle-lang" %% "axle-core" % "${releaseVersion}"
</pre>
In addition to <code>axle-core</code> the artifacts
<code>axle-algorithms</code>, <code>axle-games</code>,
<code>axle-visualize</code>, <code>axle-jblas</code>, <code>axle-joda</code>,
and <code>axle-jung</code> are also published to Sonatype
See the {RoadMap.link()} for more information on the release schedule.
      </div>))

}