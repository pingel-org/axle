
resolvers += Resolver.url("sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

resolvers += Classpaths.typesafeResolver

addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.4.3")

// addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.0.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.9.1")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.3.2")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.5.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.3")
// addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

// addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.5")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")
