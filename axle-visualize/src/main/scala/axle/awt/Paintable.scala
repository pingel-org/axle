package axle.awt

import java.awt.Graphics2D
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Paintable[${P}]")
trait Paintable[P] {

  def paint(p: P, g2d: Graphics2D): Unit

}

object Paintable {

  final def apply[P](implicit paintable: Paintable[P]): Paintable[P] = paintable

}
