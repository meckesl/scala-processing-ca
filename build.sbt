lazy val commonSettings = Seq(
  organization := "com.lms",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(
    name := "scalaBootstrap"
  )
