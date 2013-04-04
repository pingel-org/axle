package axle.algebra

// TODO find the name for this from mathematics

trait Portionable[T] {

  def portion(left: T, v: T, right: T): Double

  def tics(from: T, to: T): Seq[(T, String)]
}
