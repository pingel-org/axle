package axle.actor

import scala.concurrent.duration.DurationInt

// import akka.util.Timeout

object Defaults {

  // implicit val askTimeout = Timeout(1.second)

  implicit val askTimeout = 1.second

}
