lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      scalaVersion := "3.3.0",
      version := "0.1.0-SNAPSHOT"
    )
  ),
  name := "validation-examples",
  resolvers += Resolver.mavenLocal,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.9.0",
    "org.scalactic" %% "scalactic" % "3.2.16",
    "org.scalatest" %% "scalatest" % "3.2.15" % "test"
  )
)
