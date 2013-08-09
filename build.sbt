organization := "de.aktey.scanndal"

name := "scanndal"

version := "0.8"

scalaVersion := "2.10.2"

homepage := Some(url("https://github.com/ouven/scanndal/wiki"))

licenses := Seq("Apache License Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"),
 				"The New BSD License" -> url("http://www.opensource.org/licenses/bsd-license.html"))

libraryDependencies += "org.specs2" %% "specs2" % "latest.release" % "test"

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

scalacOptions ++= Seq("-deprecation", "-feature")

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
	<issueManagement>
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
)

