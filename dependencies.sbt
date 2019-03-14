resolvers += "jitpack" at "https://jitpack.io"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

libraryDependencies ++= {
  Seq(
    "com.github.tamer316" %% "microservice-lib" % "1.0.3",
    "com.github.tamer316" %% "amazon-ses-client" % "1.0.0"
  )
}

Revolver.settings