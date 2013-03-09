package axle.visualize

import akka.actor.{ Actor, ActorLogging, ActorRef }
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import System.currentTimeMillis

object DataFeed {
  case class Update()
  case class Fetch()
}

class DataFeedActor[T](f: () => T) extends Actor with ActorLogging {

  import DataFeed._

  val feedPump = context.system.scheduler.schedule(0.millis, 200.millis, self, Update())  
  
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