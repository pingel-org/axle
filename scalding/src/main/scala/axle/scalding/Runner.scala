package axle.scalding

import org.apache.hadoop
import cascading.tuple.Tuple
import annotation.tailrec
import com.twitter.scalding.{ Args, Job, Mode }
import com.twitter.scalding.TextLine

class Runner extends hadoop.conf.Configured with hadoop.util.Tool {

  var rootJob: Option[(Args) => Job] = None

  def run(args: Array[String]): Int = {
    val jobArgs = Args((new hadoop.util.GenericOptionsParser(getConf, args)).getRemainingArgs)
    Mode.mode = Mode(jobArgs, getConf)
    val job = if (rootJob.isDefined) {
      rootJob.get.apply(jobArgs)
    } else {
      val jobName = jobArgs.positional(0)
      val nonJobNameArgs = jobArgs + ("" -> jobArgs.positional.tail)
      Job(jobName, nonJobNameArgs)
    }
    // job.buildFlow.writeDOT(job.getClass.getName + ".dot")
    start(job, 0)
    0
  }

  @tailrec
  private[this] def start(j: Job, cnt: Int): Unit =
    if (j.run) {
      j.next match {
        case Some(nextj) => start(nextj, cnt + 1)
        case None => Unit
      }
    } else {
      throw new RuntimeException("Job failed to run: " + j.getClass.getName +
        (if (cnt > 0) (" child: " + cnt.toString + ", class: " + j.getClass.getName) else "")
      )
    }

}

object Runner {
  def main(args: Array[String]) {

    // val classNameToRun = "axle.scalding.Tutorial"
    // val classNameToRun = "axle.scalding.TypedTutorial"
    val classNameToRun = "axle.scalding.MatrixTutorial"

    hadoop.util.ToolRunner.run(
      new hadoop.conf.Configuration,
      new Runner,
      Vector(classNameToRun, "--local").toArray)
  }
}
