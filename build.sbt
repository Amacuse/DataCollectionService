name := "data-collect-service"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  evolutions,
  javaJpa,
  cache,
  javaWs,
  "org.postgresql" % "postgresql" % "9.4.1212.jre7",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.11.Final",
  "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3",
  "org.jadira.usertype" % "usertype.core" % "6.0.1.GA",
  "com.typesafe.akka" %% "akka-actor" % "2.3.7",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.7",
  "com.typesafe.akka" %% "akka-contrib" % "2.3.7"
)

initialize := {
  val _ = initialize.value
}

