name := "pingel.org test http"

version := "1.0"

scalaVersion := "2.8.1"

seq(WebPlugin.webSettings :_*)

seq(sbtassembly.Plugin.assemblySettings: _*)

libraryDependencies ++= Seq(
  "com.mongodb.casbah" %% "casbah" % "2.1.5.0",
  "org.scalatra" %% "scalatra" % "2.0.0-SNAPSHOT",
  "org.scalatra" %% "scalatra-specs" % "2.0.0-SNAPSHOT" % "test",
  "org.mortbay.jetty" % "jetty" % "6.1.22" % "jetty",
  "javax.servlet" % "servlet-api" % "2.5" % "provided->default"
)

resolvers ++= Seq(
  "Sonatype OSS" at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Web plugin repo" at "http://siasia.github.com/maven2"
)