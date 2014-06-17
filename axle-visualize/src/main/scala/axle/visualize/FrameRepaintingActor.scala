package axle.visualize

import javax.swing.JFrame
import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorRef
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class FrameRepaintingActor(frame: JFrame, dataFeedActor: ActorRef)
  extends Actor
  with ActorLogging {

  import FrameProtocol._
  import DataFeedProtocol._ 
  
  // println(s"+++ sending RegisterViewer to $dataFeedActor")
  dataFeedActor ! RegisterViewer()

  context.system.scheduler.schedule(0.millis, 42.millis, self, RepaintIfDirty())

  var dirty = true

  def receive: Receive = {
    case RepaintIfDirty() => {
      // log info ("component painter received paint message")
      // println(s"+++ FrameRepaintingActor got RepaintIfDirty")
      if (dirty) {
        // println(s"... dirty == $dirty")
        frame.repaint()
        dirty = false
      }
    }
    case Soil() => {
      //println(s"+++ FrameRepaintingActor got Soil")
      dirty = true
    }
    case msg @ _ => {
      log error (s"FrameRepaintingActor got unhandled message $msg")
    }
  }

}
