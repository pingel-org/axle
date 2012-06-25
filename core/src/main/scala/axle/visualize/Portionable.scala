package axle.visualize

trait Portionable[T] {

  // TODO find the name for this from mathematics

  def portion(left: T, v: T, right: T): Double

  def tics(from: T, to: T): Seq[(T, String)]
}
