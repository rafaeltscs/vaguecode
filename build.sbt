name := """vaguecode"""
organization := "vaguecode"

version := "1.0.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq( guice, jdbc, evolutions,
  "com.h2database" % "h2" % "1.4.197"
)
