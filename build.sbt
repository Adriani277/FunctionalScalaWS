import Dependencies._

ThisBuild / scalaVersion := "3.2.2"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.functionalscalaws"
ThisBuild / organizationName := "functionalscalaws"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .settings(
    name := "FunctionalScalaWS"
  )

val http4sVersion     = "0.21.2"
val pureConfigVersion = "0.12.3"
val zioVersion        = "2.0.8"

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.3"

coverageExcludedPackages := "functionalscalaws.Main; functionalscalaws.http.HttpServer; functionalscalaws.Layers"

libraryDependencies ++= Seq(
  "dev.zio"              % "zio_3"                     % zioVersion,
  // "io.github.gaelrenoux" %% "tranzactio"              % "2.0.0",
  // "io.d11"               %% "zhttp"                   % "0.0.4",
  // "dev.zio"              %% "zio-interop-cats"        % "2.3.1.0",
  "dev.zio"              % "zio-json_3"                % "0.4.2",
  // "dev.zio"              % "zio-json-interop-http4s" % "0.4.2",
  // "dev.zio"              %% "zio-config-magnolia"     % "3.0.7",
  // "dev.zio"              %% "zio-config"              % "3.0.7",
  // "dev.zio"              %% "zio-config-typesafe"     % "3.0.7",
  // // "org.tpolecat"         %% "doobie-h2"               % "0.9.0",
  // "org.tpolecat"         %% "doobie-core"             % "0.8.8",
  // "org.tpolecat"         %% "doobie-hikari"           % "0.8.8",
  // "dev.zio"              %% "zio-logging-slf4j"       % "2.1.9",
  // "ch.qos.logback"       % "logback-classic"          % "1.2.3",
  "mysql"                % "mysql-connector-java"     % "8.0.17",
  //Test
  "dev.zio" %% "zio-macros"   % zioVersion % "test",
  "dev.zio" %% "zio-test"     % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt" % zioVersion % "test"
)

testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

// scalacOptions ++= Seq(
//   "-Xfatal-warnings",
//   "-deprecation",
//   "-unchecked",
//   "-language:higherKinds",
//   "-Wdead-code",
//   "-Wunused:privates",
//   "-Wunused:locals",
//   "-Wunused:explicits",
//   "-Wunused:params",
//   "-Xlint:unused",
//   "-Ymacro-annotations"
// )
Global / onChangedBuildSource := ReloadOnSourceChanges
