package axle.visualize

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import axle.quanta.Time

trait Fed[T] {

  def initialValue: T
  
  var dataFeedActorOpt: Option[ActorRef] = None

  def setFeeder(fn: T => T, interval: Time.Q, system: ActorSystem): Unit = {
    dataFeedActorOpt = Some(system.actorOf(Props(new DataFeedActor(initialValue, fn, interval))))
  }

  def feeder: Option[ActorRef] = dataFeedActorOpt
  
}
