package axle.visualize

import akka.actor.{ Actor, ActorLogging }
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object DataFeed {
  case class Update()
  case class Fetch(t: Long)
}

class DataFeedActor[T](f: () => T) extends Actor with ActorLogging {

  import DataFeed._

  val feedPump = context.system.scheduler.schedule(100.millis, 200.millis, self, Update())  
  
  var lastUpdate = System.currentTimeMillis
  var data = f()

  def receive = {

    case Update() => {
      lastUpdate = System.currentTimeMillis
      data = f()
      // log info (s"Updated data behind feed at $lastUpdate")
    }

    case Fetch(t: Long) => {
      // log info (s"Checking for new feed updates since $t")
      sender ! (if (lastUpdate > t) Some(data) else None)
    }
  }

}