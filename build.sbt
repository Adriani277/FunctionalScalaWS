ThisBuild / scalaVersion      := "3.1.2"
ThisBuild / version           := "0.1.0-SNAPSHOT"
ThisBuild / organization      := "com.functionalscalaws"
ThisBuild / organizationName  := "functionalscalaws"
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .settings(
    name := "FunctionalScalaWS"
  )

val http4sVersion     = "0.21.2"
val pureConfigVersion = "0.12.3"

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

coverageExcludedPackages := "functionalscalaws.Main; functionalscalaws.http.HttpServer; functionalscalaws.Layers"

libraryDependencies ++= Seq(
  "dev.zio"       %% "zio"                 % "2.0.0-RC5",
  "dev.zio"       %% "zio-interop-cats"    % "3.3.0-RC5",
  "dev.zio"       %% "zio-config-magnolia" % "3.0.0-RC8",
  "dev.zio"       %% "zio-config-typesafe" % "3.0.0-RC8",
  "dev.zio"       %% "zio-logging-slf4j"   % "2.0.0-RC8",
  "io.d11"        %% "zhttp"               % "2.0.0-RC7",
  "ch.qos.logback" % "logback-classic"     % "1.2.3",
  //Test
  "io.d11"  %% "zhttp-test"   % "2.0.0-RC7" % "test",
  "dev.zio" %% "zio-test"     % "2.0.0-RC5" % "test",
  "dev.zio" %% "zio-test-sbt" % "2.0.0-RC5" % "test"
)

testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-language:higherKinds"
  // "-Wdead-code",
)
