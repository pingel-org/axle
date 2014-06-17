package axle.actor

import akka.actor.{ Props, Actor, ActorRef, ActorSystem, ActorLogging }
import akka.util.Timeout
import scala.concurrent.duration._

object Defaults {

  //val system = ActorSystem("AxleAkkaActorSystem")

  implicit val askTimeout = Timeout(1.second)

}
