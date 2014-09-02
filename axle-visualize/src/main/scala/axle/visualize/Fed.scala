package axle.visualize

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import axle.quanta2.Time
import axle.quanta2.UnittedQuantity

trait Fed[T] {

  def initialValue: T
  
  var dataFeedActorOpt: Option[ActorRef] = None

  def setFeeder(fn: T => T, interval: UnittedQuantity[Time, Double], system: ActorSystem): ActorRef = {
    val feederActorRef = system.actorOf(Props(new DataFeedActor(initialValue, fn, interval)))
    dataFeedActorOpt = Some(feederActorRef)
    feederActorRef
  }

  def feeder: Option[ActorRef] = dataFeedActorOpt
  
}
