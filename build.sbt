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

val http4sVersion = "0.21.2"

libraryDependencies ++= Seq(
  "org.typelevel"     %% "cats-core"           % "2.1.1",
  "org.typelevel"     %% "cats-effect"         % "2.1.2",
  "org.http4s"        %% "http4s-dsl"          % http4sVersion,
  "org.http4s"        %% "http4s-blaze-server" % http4sVersion,
  "io.chrisdavenport" %% "log4cats-slf4j"      % "1.0.1",
  "ch.qos.logback"    % "logback-classic"      % "1.2.3",
  //Test
  "org.scalatest"     %% "scalatest"       % "3.1.1" % "test",
  "org.scalatestplus" %% "scalacheck-1-14" % "3.1.1.1",
  "org.scalacheck"    %% "scalacheck"      % "1.14.3",
  "com.olegpy"        %% "meow-mtl-core"   % "0.4.0"
)

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-language:higherKinds",
  "-Wdead-code",
  "-Wunused:privates",
  "-Wunused:locals",
  "-Wunused:explicits",
  "-Wunused:params",
  "-Xlint:unused",
  "-Wunused:imports"
)
