package axle.visualize

import java.awt.Graphics2D

trait Paintable {
  
  def paint(g2d: Graphics2D): Unit
  
}
