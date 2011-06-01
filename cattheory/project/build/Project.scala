
import sbt._
import de.element34.sbteclipsify._

class Project(info: ProjectInfo) extends DefaultProject(info)
with assembly.AssemblyBuilder
with Eclipsify
{
  // val commonsLang = "commons-lang" % "commons-lang" % "2.6"

  val slf4jsimple = "org.slf4j" % "slf4j-simple" % "1.6.1"

  val slf4japi = "org.slf4j" % "slf4j-api" % "1.6.1"

  val scalaz = "org.scalaz" % "scalaz-core_2.8.1" % "6.0-SNAPSHOT"

  val glassFishRepo = "GlassFish Maven Repository" at "http://download.java.net/maven/glassfish"

  val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"

  override def managedStyle = ManagedStyle.Maven

  val keyFile: java.io.File = Path.userHome / ".ec2" / "id_rsa-gsg-keypair" asFile

  lazy val publishTo = Resolver.ssh("pingel.org-repo",
				    "www.pingel.org",
				    "/var/www/pingel.org/maven2") as("root", keyFile) withPermissions("0644")

}
