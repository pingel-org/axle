package axle.awt

import java.awt.Component
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Draw[${T}]")
trait Draw[T] {

  def component(t: T): Component
}

object Draw {

  final def apply[T](implicit draw: Draw[T]): Draw[T] = draw
}