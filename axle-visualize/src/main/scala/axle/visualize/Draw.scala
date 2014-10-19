package axle.visualize

import java.awt.Component

trait Draw[T] {

  def component(t: T): Component
}