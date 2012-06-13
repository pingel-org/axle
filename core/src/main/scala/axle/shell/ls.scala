package axle.shell

import java.io._
import axle.ShellCommand

case class ls(argPath: String = ".", long: Boolean = false, all: Boolean = false)
  extends ShellCommand {

  def fileLine(file: File): String = {
    // Note: JDK 7 will support a proper stat http://openjdk.java.net/projects/nio/javadoc/java/nio/file/attribute/PosixFileAttributes.html
    val fileMode = "-rwxr-xr-x" // TODO
    val numLinks = 1 // TODO
    val ownerName = "pingel" // TODO
    val groupName = "staff" // TODO

    val numBytes = file.length

    val mtime = file.lastModified
    val mtimeAbbreviatedMonth = "May"
    val mtimeDayOfMonth = " 6"
    val mtimeHour = "19"
    val mtimeMinute = "51"

    val pathname = file.getPath()

    val mTimeStr = mtimeAbbreviatedMonth + " " + mtimeDayOfMonth + " " + mtimeHour + ":" + mtimeMinute

    List(fileMode, numLinks, ownerName, groupName, numBytes, mTimeStr, pathname).mkString(" ")
  }

  def exec(): Int = {
    val file = new File(argPath)
    if (file.isDirectory) {
      for ((_, children) <- file.listFiles.groupBy(_.isDirectory)) {
        for (child <- children.toList.sortWith(_.getPath() < _.getPath())) {
          if (all || !child.isHidden) {
            stdout.println(fileLine(child))
          }
        }
      }
    } else {
      stdout.println(fileLine(file))
    }
    0
  }
}
