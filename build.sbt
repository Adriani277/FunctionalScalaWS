import Dependencies._

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.functionalscalaws"
ThisBuild / organizationName := "functionalscalaws"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .settings(
    name := "FunctionalScalaWS",
    libraryDependencies += scalaTest % Test
  )

val http4sVersion     = "0.21.2"
val pureConfigVersion = "0.12.3"
val zioVersion        = "1.0.7"

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.3"

coverageExcludedPackages := "functionalscalaws.Main; functionalscalaws.http.HttpServer; functionalscalaws.Layers"

libraryDependencies ++= Seq(
  "dev.zio"              %% "zio"                     % zioVersion,
  "io.github.gaelrenoux" %% "tranzactio"              % "2.0.0",
  "io.d11"               %% "zhttp"                   % "1.0.0.0-RC15",
  "dev.zio"              %% "zio-interop-cats"        % "2.3.1.0",
  "io.github.kitlangton" %% "zio-magic"               % "0.1.12",
  "dev.zio"              %% "zio-json"                % "0.1.4",
  "dev.zio"              %% "zio-json-interop-http4s" % "0.1.4",
  "dev.zio"              %% "zio-config-magnolia"     % "1.0.4",
  "dev.zio"              %% "zio-config"              % "1.0.4",
  "dev.zio"              %% "zio-config-typesafe"     % "1.0.4",
  "org.tpolecat"         %% "doobie-h2"               % "0.9.0",
  "org.tpolecat"         %% "doobie-core"             % "0.8.8",
  "org.tpolecat"         %% "doobie-hikari"           % "0.8.8",
  "dev.zio"              %% "zio-logging-slf4j"       % "0.5.3",
  "io.chrisdavenport"    %% "log4cats-slf4j"          % "1.0.1",
  "ch.qos.logback"       % "logback-classic"          % "1.2.3",
  "mysql"                % "mysql-connector-java"     % "8.0.17",
  //Test
  "dev.zio" %% "zio-macros"   % zioVersion % "test",
  "dev.zio" %% "zio-test"     % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt" % zioVersion % "test"
)

testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

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
  "-Wconf:cat=unused:info",
  "-Ymacro-annotations"
)
