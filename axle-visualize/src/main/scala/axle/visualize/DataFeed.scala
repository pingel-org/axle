package axle.visualize

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationDouble
import scala.concurrent.duration.DurationInt

import DataFeedProtocol.Fetch
import DataFeedProtocol.Recompute
import DataFeedProtocol.RegisterViewer
import FrameProtocol.Soil
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import axle.quanta2.Time
import axle.quanta2.Time.millisecond
import axle.quanta2.Quantity
import spire.implicits.DoubleAlgebra 

case class DataFeedActor[T](initialValue: T, refreshFn: T => T, interval: Quantity[Time, Double])
  extends Actor
  with ActorLogging {

  import DataFeedProtocol._
  import FrameProtocol._

  context.system.scheduler.schedule(
    0.millis,
    ((interval in millisecond[Double]).magnitude).millis,
    self,
    Recompute())

  var data = initialValue

  var viewers = Set.empty[ActorRef]

  def receive: Receive = {

    case RegisterViewer() => {
      //println("DataFeed got RegisterViewer")
      viewers = viewers + sender
    }

    case Recompute() => {
      //println("DataFeed got Recompute")
      data = refreshFn(data)
      viewers.foreach(_ ! Soil())
      // log info (s"Updated data behind feed at $lastUpdate")
    }

    case Fetch() => {
      //println("DataFeed got Fetch")
      // log info (s"Checking for new feed updates since $t")
      // log info ("sender.path.name: " + sender.path.name)
      sender ! data
    }

    case msg @ _ => {
      log error (s"DataFeedActor got unhandled message $msg")
    }

  }

}
