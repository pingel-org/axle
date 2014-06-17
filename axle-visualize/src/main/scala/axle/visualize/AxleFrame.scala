package axle.visualize

import javax.swing.{ JFrame, JPanel }
import java.awt.{ Dimension, Component, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import akka.actor.{ Props, Actor, ActorRef, ActorLogging, ActorSystem }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
//import axle.actor.Defaults._

trait FrameProtocol {

  case class RepaintIfDirty()
  case class Soil()
}

class FrameRepaintingActor(frame: JFrame, dataFeedActor: ActorRef)
  extends Actor
  with ActorLogging
  with FrameProtocol
  with DataFeedProtocol {

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
  title: String)
  extends JFrame(title) {

  def initialize(): Unit = {
    setBackground(bgColor)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(width, height)
    val bg = add(new BackgroundPanel(title))
    bg.setVisible(true)
  }

}
