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

ThisBuild / githubWorkflowAddedJobs ++= Seq(WorkflowJob(
  "release",
  "Release",
  List(
    WorkflowStep.Checkout,
    WorkflowStep.Use(
      UseRef.Public("actions", "setup-java", "v3")
    ),
    WorkflowStep.Sbt(List("Universal/stage")),
    WorkflowStep.Run(
      id = Option("tagname"),
      commands = List("echo \"RELEASE_VERSION=${GITHUB_REF#refs/*/}\" >> $GITHUB_ENV"),
      cond = Option("github.event_name == 'push' && contains(github.ref, 'refs/tags')"),
    ),
    WorkflowStep.Use(
      UseRef.Public("TheDoctor0", "zip-release", "0.7.1"),
      cond = Option("github.event_name == 'push' && contains(github.ref, 'refs/tags')"),
      params = Map(
        "type" -> "zip",
        "filename" -> "release-${{steps.tagname.outputs.tag}}"
      )
    ),
  ),
  scalas = List(scala3Version),
))

ThisBuild / githubWorkflowPublishTargetBranches := Seq()
ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowArtifactUpload := false

