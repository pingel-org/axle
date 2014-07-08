package axle.visualize

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

import DataFeedProtocol.RegisterViewer
import FrameProtocol.RepaintIfDirty
import FrameProtocol.Soil
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import javax.swing.JFrame
  
class FrameRepaintingActor(frame: JFrame, dataFeedActor: ActorRef)
  extends Actor
  with ActorLogging {
  
  dataFeedActor ! RegisterViewer()

  context.system.scheduler.schedule(0.millis, 42.millis, self, RepaintIfDirty())

  var dirty = true

  def receive: Receive = {
    case RepaintIfDirty() => {
      // log info ("component painter received paint message")
      if (dirty) {
        frame.repaint()
        dirty = false
      }
    }
    case Soil() => {
      dirty = true
    }
    case msg @ _ => {
      log error (s"FrameRepaintingActor got unhandled message $msg")
    }
  }

}
