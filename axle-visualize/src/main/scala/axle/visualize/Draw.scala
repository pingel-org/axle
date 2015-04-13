package axle.visualize

import java.awt.Component
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Draw[${T}]")
trait Draw[T] {

  def component(t: T): Component
}

object Draw {
  
  def apply[T: Draw]: Draw[T] = implicitly[Draw[T]]
}