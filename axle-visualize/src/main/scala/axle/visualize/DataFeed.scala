package axle.visualize

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem }
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import axle.quanta.Time
import System.currentTimeMillis

case class DataFeedActor[T](initialValue: T, refreshFn: T => T, interval: Time.Q)
  extends Actor
  with ActorLogging {

  import DataFeedProtocol._
  import FrameProtocol._
  
  context.system.scheduler.schedule(
    0.millis,
    ((interval in Time.millisecond).magnitude.doubleValue).millis,
    self,
    Recompute())

  var data = initialValue

  var viewers = Set.empty[ActorRef]

  def receive: Receive = {

    case RegisterViewer() => {
      viewers = viewers + sender
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

    case msg @ _ => {
      log error (s"DataFeedActor got unhandled message $msg")
    }

  }

}
