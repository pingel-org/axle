package axle.visualize

import akka.actor.ActorRef

trait Fed {
  def feeder: Option[ActorRef]
}
