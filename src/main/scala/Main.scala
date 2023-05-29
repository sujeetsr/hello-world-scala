import scala.io.StdIn.readLine
import sys.process._

@main def hello: Unit =
  val tags = "git tag --list --sort=-version:refname --merged".!!
  val tagList = tags.split("\\r?\\n").map(_.trim)
  val tag = if tagList.length > 0 && tagList(0).length > 0 then tagList(0) else "0.0.0"
  println(s"${tag}")



 

