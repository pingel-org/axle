package axle.visualize

import javax.swing.{ JFrame, JPanel }
import java.awt.{ Dimension, Component, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import akka.actor.{ Props, Actor, ActorRef, ActorLogging }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import axle.actor.Defaults._

object FrameProtocol {

  case class RepaintIfDirty()
  case class Soil()
}

class FrameRepaintingActor(frame: JFrame, dataFeedActorOpt: Option[ActorRef]) extends Actor with ActorLogging {

  import FrameProtocol._
  import DataFeedProtocol._

  dataFeedActorOpt.map(_ ! RegisterViewer())

  context.system.scheduler.schedule(0.millis, 42.millis, self, RepaintIfDirty())

  var dirty = true

  def receive = {
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
  }

}

class BackgroundPanel(title: String) extends JPanel {

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    val g2d = g.asInstanceOf[Graphics2D]
    g2d.setColor(Color.black)
    g2d.drawString(title, 20, 20)
  }

}

class AxleFrame(
  width: Int,
  height: Int,
  bgColor: Color,
  title: String,
  dataFeedActorOpt: Option[ActorRef])
  extends JFrame(title) {

  val frameRepaintingActor = system.actorOf(Props(new FrameRepaintingActor(this, dataFeedActorOpt)))

  def initialize(): Unit = {
    setBackground(bgColor)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(width, height)
    val bg = add(new BackgroundPanel(title))
    bg.setVisible(true)
  }

}
