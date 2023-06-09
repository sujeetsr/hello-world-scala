val scala2Version = "2.13.10"
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

// ThisBuild / githubWorkflowAddedJobs ++= Seq(WorkflowJob(
//   "release",
//   "Release",
//   List(
//     WorkflowStep.Checkout,
//     WorkflowStep.Sbt(List("Universal/stage")),
//     WorkflowStep.Run(
//       id = Option("release_tag"),
//       commands = List(
//         "echo \"release_tag=${GITHUB_REF#refs/*/}\" >> $GITHUB_OUTPUT",
//         "echo \"release_tag=${GITHUB_REF#refs/*/}\""
//       ),
//       cond = Option("github.event_name == 'push' && contains(github.ref, 'refs/tags')"),
//     ),
//     WorkflowStep.Run(
//       id = Option("previous_tag"),
//       commands = List(
//         "git fetch --prune --unshallow --tags",
//         "echo tag=`git tag --list \"v*\" --sort=-version:refname --merged | head -n 2 | tail -n 1` >> $GITHUB_OUTPUT",
//         "echo tag=`git tag --list \"v*\" --sort=-version:refname --merged | head -n 2 | tail -n 1`",
//         "git tag --list \"v*\" --sort=-version:refname --merged"
//       ),
//       cond = Option("github.event_name == 'push' && contains(github.ref, 'refs/tags')"),
//     ),
//     WorkflowStep.Use(
//       UseRef.Public("madhead", "semver-utils", "latest"),
//       params = Map(
//         "version" -> "${{steps.release_tag.outputs.release_tag}}",
//         "compare-to" -> "${{steps.previous_tag.outputs.tag}}"
//       ),
//       id = Option("validate_tag"),
//       cond = Option("github.event_name == 'push' && contains(github.ref, 'refs/tags')"),
//     ),
//     WorkflowStep.Use(
//       UseRef.Public("TheDoctor0", "zip-release", "0.7.1"),
//       params = Map(
//         "type" -> "zip",
//         "filename" -> "release-${{steps.release_tag.outputs.release_tag}}",
//       ),
//       cond = Option(
//         "github.event_name == 'push' && " +
//         "contains(github.ref, 'refs/tags') && " +
//         "steps.validate_tag.outputs.comparison-result == '>'"
//       )
//     ),
//     WorkflowStep.Use(
//       UseRef.Public("ncipollo", "release-action", "v1"),
//       params = Map(
//         "artifacts" -> "release-${{steps.release_tag.outputs.release_tag}}",
//         "tag" ->  "${{steps.release_tag.outputs.release_tag}}",
//         "generateReleaseNotes" -> "${{true}}"
//       ),
//       cond = Option(
//         "github.event_name == 'push' && " +
//         "contains(github.ref, 'refs/tags') && " +
//         "steps.validate_tag.outputs.comparison-result == '>'"
//       )
//     )
//   ),
//   needs = List("build"),
//   scalas = List(scala3Version),
// ))
ThisBuild / githubWorkflowTargetBranches := Seq("main")
ThisBuild / githubWorkflowPublishTargetBranches := Seq()
ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowArtifactUpload := false

