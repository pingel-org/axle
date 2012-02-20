resolvers += Classpaths.typesafeResolver

resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
    
addSbtPlugin("org.ensime" % "ensime-sbt-cmd" % "0.0.7")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.0.0-M3")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.7.2")
