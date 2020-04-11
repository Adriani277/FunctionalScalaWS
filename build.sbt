import Dependencies._

ThisBuild / scalaVersion := "2.13.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.functionalscalaws"
ThisBuild / organizationName := "functionalscalaws"

lazy val root = (project in file("."))
  .settings(
    name := "FunctionalScalaWS",
    libraryDependencies += scalaTest % Test
  )

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core"     % "2.1.1",
  "org.typelevel" %% "cats-effect"   % "2.1.2",
  "org.scalatest" % "scalatest_2.13" % "3.1.1" % "test"
)
