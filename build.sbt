
lazy val scanndal = project.in(file(".")).settings(Seq(
  organization := "de.aktey.scanndal",
  name := "scanndal",

  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.10.4", "2.11.8"),

  scalacOptions ++= Seq("-deprecation", "-feature"),

  homepage := Some(url("https://github.com/ouven/scanndal/wiki")),

  licenses := Seq(
    "Apache License Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"),
    "The New BSD License" -> url("http://www.opensource.org/licenses/bsd-license.html")
  ),

  resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test",

    // relase with sbt-pgp plugin
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseProcess := ReleaseProcess.steps,
  releaseCrossBuild := true,

  publishTo <<= version { v: String =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },

  publishMavenStyle := true,

  scalacOptions ++= Seq("-deprecation", "-feature"),

  publishArtifact in Test := false,

  pomIncludeRepository := { _ => false },

  pomExtra := <issueManagement>
    <system>github</system>
    <url>https://github.com/ouven/scanndal/issues</url>
  </issueManagement>
    <developers>
      <developer>
        <name>Ruben Wagner</name>
        <url>https://github.com/ouven</url>
        <roles>
          <role>owner</role>
          <role>developer</role>
        </roles>
        <timezone>+1</timezone>
      </developer>
    </developers>
    <scm>
      <url>git@github.com:ouven/scanndal.git</url>
      <connection>scm:git:git@github.com:ouven/scanndal.git</connection>
      <developerConnection>scm:git:git@github.com:ouven/scanndal.git</developerConnection>
    </scm>
))


