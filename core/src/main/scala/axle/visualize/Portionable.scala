package axle.visualize

trait Portionable[T] {
  def portion(left: T, v: T, right: T): Double
}
