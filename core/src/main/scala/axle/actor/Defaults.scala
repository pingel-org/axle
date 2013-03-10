package axle.akka

import akka.actor.{ Props, Actor, ActorRef, ActorSystem, ActorLogging }
//import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
//import scala.concurrent.Await

object Defaults {

  val system = ActorSystem("AxleAkkaActorSystem")

  implicit val askTimeout = Timeout(1.second)

}