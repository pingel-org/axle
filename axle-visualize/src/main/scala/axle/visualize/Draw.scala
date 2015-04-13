package axle.visualize

import java.awt.Component
import scala.annotation.implicitNotFound

@implicitNotFound("No member of typeclass Draw found for types ${T}")
trait Draw[T] {

  def component(t: T): Component
}

object Draw {
  
  def apply[T: Draw]: Draw[T] = implicitly[Draw[T]]
}