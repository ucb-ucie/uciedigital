/*
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

lazy val root = (project in file("."))
  .settings(
    name := "uciedigital",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % chiselVersion,
      "edu.berkeley.cs" %% "chiseltest" % "0.6.2" % Test,
      "org.scalatest" %% "scalatest" % "3.2.17" % Test,
    ),
    addCompilerPlugin(
      "edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full,
    ),
  )

// Plugins
Global / excludeLintKeys += idePackagePrefix
root / idePackagePrefix := Some("edu.berkeley.cs.ucie.digital")
*/
name := "ucie_digital"
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

addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full)
libraryDependencies ++= Seq(
  "edu.berkeley.cs" %% "rocketchip-3.6.0" % "1.6-3.6.0-e3773366a-SNAPSHOT",
  "edu.berkeley.cs" %% "chisel3" % chiselVersion,
  "edu.berkeley.cs" %% "chiseltest" % "0.6.2" % Test,
  "org.scalatest" %% "scalatest" % "3.2.17" % Test,
)

resolvers ++= Resolver.sonatypeOssRepos("snapshots")
resolvers ++= Resolver.sonatypeOssRepos("releases")
resolvers += Resolver.mavenLocal
// Plugins
Global / excludeLintKeys += idePackagePrefix
idePackagePrefix := Some("edu.berkeley.cs.ucie.digital")

import Tests._

Test / fork := true
Test / testGrouping := (Test / testGrouping).value.flatMap { group =>
   group.tests.map { test =>
      Group(test.name, Seq(test), SubProcess(ForkOptions()))
   }
}

concurrentRestrictions := Seq(Tags.limit(Tags.ForkedTestGroup, 72))
