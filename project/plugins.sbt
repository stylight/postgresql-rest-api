resolvers += Classpaths.sbtPluginReleases

// Plugin for scoverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "0.99.7.1")

// Plugin for publishing scoverage results to coveralls
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "0.99.0")

//for building fat-jar
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.1")

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.2.0")

// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
// resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.1")
