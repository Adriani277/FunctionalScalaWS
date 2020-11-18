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

val http4sVersion     = "0.21.2"
val pureConfigVersion = "0.12.3"

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.3"

coverageExcludedPackages := "functionalscalaws.Main; functionalscalaws.http.HttpServer; functionalscalaws.Layers"

libraryDependencies ++= Seq(
  "dev.zio"           %% "zio"                 % "1.0.3",
  "dev.zio"           %% "zio-interop-cats"    % "2.2.0.1",
  "dev.zio"           %% "zio-config-magnolia" % "1.0.0-RC29",
  "dev.zio"           %% "zio-config-typesafe" % "1.0.0-RC29",
  "dev.zio"           %% "zio-logging-slf4j"   % "0.5.3",
  "org.http4s"        %% "http4s-dsl"          % http4sVersion,
  "org.http4s"        %% "http4s-blaze-server" % http4sVersion,
  "io.chrisdavenport" %% "log4cats-slf4j"      % "1.0.1",
  "ch.qos.logback"    % "logback-classic"      % "1.2.3",
  "org.http4s"        %% "http4s-circe"        % http4sVersion,
  "io.circe"          %% "circe-generic"       % "0.12.3",
  //Test
  "dev.zio" %% "zio-test"     % "1.0.3" % "test",
  "dev.zio" %% "zio-test-sbt" % "1.0.3" % "test"
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
  "-Wunused:imports",
  "-Ymacro-annotations"
)
