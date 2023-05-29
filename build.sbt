val scala3Version = "3.2.2"

lazy val startupTransition: State => State = "conventionalCommits" :: _

lazy val root = project
  .in(file("."))
  .settings(
    name := "hello-world",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    Global / onLoad := {
      val old = (Global / onLoad).value
      startupTransition compose old
    }
  )

enablePlugins(JavaAppPackaging)

ThisBuild / scalaVersion                                   := scala3Version
ThisBuild / crossScalaVersions                             := Seq(scala3Version)
ThisBuild / githubWorkflowPublishTargetBranches := Seq()
ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowArtifactUpload := false

