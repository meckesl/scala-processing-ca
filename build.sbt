lazy val commonSettings = Seq(
  organization := "com.lms",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(
    name := "scalaBootstrap",
    libraryDependencies += "org.processing" % "core" % "3.0b3",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.3",
    libraryDependencies += "org.jogamp.jogl" % "jogl-all" % "2.3.1",
    libraryDependencies += "org.jogamp.gluegen" % "gluegen-rt" % "2.3.1"
  )
