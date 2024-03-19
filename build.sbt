ThisBuild / organization := "edu.berkeley.cs"
ThisBuild / version := "0.0.1-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / scalacOptions := Seq(
  "-deprecation",
  "-feature",
  "-language:reflectiveCalls",
  "-Xcheckinit",
  "-Xlint",
)

Compile / doc / scalacOptions += "-groups"

val chiselVersion = "3.6.0"

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

lazy val root = (project in file("."))
  .settings(
    name := "uciedigital",
    idePackagePrefix := Some("edu.berkeley.cs.ucie.digital"),
    libraryDependencies ++=
      Seq(
        "edu.berkeley.cs" %% "chisel3" % chiselVersion,
        "edu.berkeley.cs" %% "chiseltest" % "0.6.2" % Test,
        "org.scalatest" %% "scalatest" % "3.2.18" % Test,
        // from Sonatype OSS Snapshot repos
        "edu.berkeley.cs" %% "rocketchip-3.6.0" % "1.6-3.6.0-e3773366a-SNAPSHOT",
      ),
    addCompilerPlugin(
      "edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full,
    ),
    Test / fork := true,
    Test / testGrouping := (Test / testGrouping).value.flatMap { group =>
      import Tests._
      group.tests.map { test =>
        Group(test.name, Seq(test), SubProcess(ForkOptions()))
      }
    },
    concurrentRestrictions := Seq(Tags.limit(Tags.ForkedTestGroup, 72)),
  )

// Plugins
Global / excludeLintKeys += idePackagePrefix
