import sbt._

// Versions
object Dependencies {
  val Scalify = new {
    val Version = "0.0.18"
    val Core = "com.github.fulrich" %% "scalify" % Version
    val PlusPlay = "com.github.fulrich" %% "scalifyplus-play" % Version
  }

  val ScalaTest = new {
    val Version = "4.0.2"
    val PlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % Version
  }

  val TestCharged = new {
    val Version = "0.1.12"
    val Core = "com.github.fulrich" %% "test-charged" % Version
  }
}