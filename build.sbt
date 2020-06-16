name := "IDriver7"
version := "0.0.1"

scalaVersion := "2.11.12"

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8")

autoScalaLibrary := false

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % scalaVersion.value,
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-encoding", "UTF-8")

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.13" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
)
