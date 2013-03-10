package axle.visualize

import akka.actor.{ Actor, ActorLogging, ActorRef }
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import axle.quanta.Time
import System.currentTimeMillis

trait Fed {
  def feeder(): ActorRef
}

object DataFeedProtocol {
  case class RegisterViewer()
  case class Recompute()
  case class Fetch()
}

class DataFeedActor[T](f: () => T, refreshInterval: Option[Time.Q]) extends Actor with ActorLogging {

  import DataFeedProtocol._

  refreshInterval.map(interval =>
    context.system.scheduler.schedule(
      0.millis,
      ((interval in Time.millisecond).magnitude.doubleValue).millis,
      self,
      Recompute()))

  var data = f()
  val viewers = collection.mutable.Set[ActorRef]()

  def receive = {

    case RegisterViewer() => {
      viewers += sender
    }

    case Recompute() => {
      data = f()
      viewers.map(_ ! FrameProtocol.Soil())
      // log info (s"Updated data behind feed at $lastUpdate")
    }

    case Fetch() => {
      // log info (s"Checking for new feed updates since $t")
      // log info ("sender.path.name: " + sender.path.name)
      sender ! data
    }
  }

}