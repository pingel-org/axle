package axle.visualize

import javax.swing.{ JFrame, JPanel }
import java.awt.{ Dimension, Component, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import akka.actor.{ Props, Actor, ActorLogging }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object FramePainter {

  case class Repaint()
}

class FrameRepaintingActor(frame: JFrame) extends Actor with ActorLogging {

  import FramePainter._

  val paintPump = context.system.scheduler.schedule(0.millis, 300.millis, self, Repaint())

  def receive = {
    case Repaint() => {
      // log info ("component painter received paint message")
      frame.repaint()
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

class AxleFrame(width: Int = 1100, height: Int = 800, bgColor: Color = Color.white, title: String = "αχλε")
  extends JFrame(title) {

  val frameRepaintingActor = AxleAkka.system.actorOf(Props(new FrameRepaintingActor(this)))
  
  def initialize(): Unit = {
    setBackground(bgColor)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(width, height)
    val bg = add(new BackgroundPanel(title))
    bg.setVisible(true)
  }

}