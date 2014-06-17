package axle.visualize

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem }
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import axle.quanta.Time
import System.currentTimeMillis

trait Fed {
  def feeder: Option[ActorRef]
}

trait DataFeedProtocol {
  case class RegisterViewer()
  case class Recompute()
  case class Fetch()
}

class DataFeedActor[T](initialValue: T, refreshFn: T => T, interval: Time.Q)
  extends Actor
  with ActorLogging
  with DataFeedProtocol
  with FrameProtocol {

  context.system.scheduler.schedule(
    0.millis,
    ((interval in Time.millisecond).magnitude.doubleValue).millis,
    self,
    Recompute())

  var data = initialValue

  val viewers = collection.mutable.Set[ActorRef]()

  def receive: Receive = {

    case RegisterViewer() => {
      viewers += sender
    }

    case Recompute() => {
      data = refreshFn(data)
      viewers.foreach(_ ! Soil())
      // log info (s"Updated data behind feed at $lastUpdate")
    }

    case Fetch() => {
      // log info (s"Checking for new feed updates since $t")
      // log info ("sender.path.name: " + sender.path.name)
      sender ! data
    }
  }

}
