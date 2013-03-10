package axle.visualize

import akka.actor.{ Actor, ActorLogging, ActorRef }
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import axle.quanta.Time
import System.currentTimeMillis

object DataFeed {
  case class Update()
  case class Fetch()
}

class DataFeedActor[T](f: () => T, refreshInterval: Option[Time.Q]) extends Actor with ActorLogging {

  import DataFeed._

  refreshInterval.map(interval =>
    context.system.scheduler.schedule(
      0.millis,
      ((interval in Time.millisecond).magnitude.doubleValue).millis,
      self,
      Update()))

  var newestVersion = currentTimeMillis
  var data = f()

  // Tracking this client -> version map here is not optimal, but is short-term solution
  val clientVersions = collection.mutable.Map[ActorRef, Long]().withDefaultValue(0L)

  def receive = {

    case Update() => {
      newestVersion = currentTimeMillis
      data = f()
      // log info (s"Updated data behind feed at $lastUpdate")
    }

    case Fetch() => {
      val t = clientVersions(sender)
      clientVersions += sender -> newestVersion
      // log info (s"Checking for new feed updates since $t")
      sender ! (if (newestVersion > t) Some(data) else None)
    }
  }

}