
import sbt._
import de.element34.sbteclipsify._

class Project(info: ProjectInfo) extends DefaultProject(info)
with assembly.AssemblyBuilder
with Eclipsify
{
  // val commonsLang = "commons-lang" % "commons-lang" % "2.6"

  val slf4jsimple = "org.slf4j" % "slf4j-simple" % "1.6.1"
  val slf4japi = "org.slf4j" % "slf4j-api" % "1.6.1"
  val glassFishRepo = "GlassFish Maven Repository" at "http://download.java.net/maven/glassfish"
}
