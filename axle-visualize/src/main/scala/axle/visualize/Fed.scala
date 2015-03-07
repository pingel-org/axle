package axle.visualize

import java.awt.Component
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import axle.quanta.Time
import axle.quanta.UnittedQuantity4
import axle.quanta.UnitOfMeasurement4
import axle.algebra.DirectedGraph

trait Fed[T] extends Component {

  def initialValue: T

  var dataFeedActorOpt: Option[ActorRef] = None

  def setFeeder[DG[_, _]: DirectedGraph](
    fn: T => T,
    interval: UnittedQuantity4[Time[Double], Double],
    system: ActorSystem)(
      implicit time: Time[Double],
      timeCg: DG[UnitOfMeasurement4[Time[Double], Double], Double => Double]): ActorRef = {
    val feederActorRef = system.actorOf(Props(new DataFeedActor(initialValue, fn, interval)))
    dataFeedActorOpt = Some(feederActorRef)
    feederActorRef
  }

  def feeder: Option[ActorRef] = dataFeedActorOpt

}
