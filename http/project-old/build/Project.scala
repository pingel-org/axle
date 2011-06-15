
import sbt._
import de.element34.sbteclipsify._

class Project(info: ProjectInfo) extends DefaultWebProject(info)
with assembly.AssemblyBuilder
with Eclipsify
{
	val description = "sample scalatra server"

  val sonatypeNexusSnapshots = "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  val sonatypeNexusReleases = "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"
  val scalatra = "org.scalatra" % "scalatra_2.8.1" % "2.0.0-SNAPSHOT"

  // val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.22" % "test"
  // val jettyWebapp6 = "org.mortbay.jetty" % "jetty-webapp" % "6.1.22" % "test"

  val jetty7 = "org.eclipse.jetty" % "jetty-webapp" % "7.4.1.v20110513" % "test"
  // val jetty7websocket = "org.eclipse.jetty" % "jetty-websocket" % "7.4.1.v20110513" % "compile"  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.22" % "test"
  // val servletApi = "org.mortbay.jetty" % "servlet-api" % "2.5-20081211" % "provided"	  
  val servletApi = "javax.servlet" % "servlet-api" % "2.5" % "provided"
	  
  // val commonsLang = "commons-lang" % "commons-lang" % "2.6"
  val slf4jsimple = "org.slf4j" % "slf4j-simple" % "1.6.1"
  val slf4japi = "org.slf4j" % "slf4j-api" % "1.6.1"
  // val sfl4jnop = "org.slf4j" % "slf4j-nop" % slf4jVersion % "runtime"
  // val scalaz = "org.scalaz" % "scalaz-core_2.8.1" % "6.0"
  // val glassFishRepo = "GlassFish Maven Repository" at "http://download.java.net/maven/glassfish"
  // override def webappClasspath = super.webappClasspath +++ buildCompilerJar
  // val scala_tools_repo = "Scala Tools" at "http://scala-tools.org/repo-snapshots/"
  // val jboss_repo = "JBoss" at "http://repository.jboss.org/nexus/content/groups/public/"
  // val akka_repo = "Akka" at "http://akka.io/repository/"
  // val blueeyesRelease = "com.github.blueeyes" % "blueeyes" % "0.3.24" % "compile"

  override def managedStyle = ManagedStyle.Maven

  val keyFile: java.io.File = Path.userHome / ".ec2" / "id_rsa-gsg-keypair" asFile

  lazy val publishTo = Resolver.ssh("pingel.org-repo",
				    "www.pingel.org",
				    "/var/www/pingel.org/maven2") as("root", keyFile) withPermissions("0644")

}
