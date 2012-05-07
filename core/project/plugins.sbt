
resolvers += Resolver.url("sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

resolvers += Classpaths.typesafeResolver

addSbtPlugin("org.ensime" % "ensime-sbt-cmd" % "0.0.7")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.0.0-M3")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.7.3")

addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.6")
