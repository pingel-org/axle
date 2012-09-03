package axle.stats

case class PMultiply(left: Probability, right: () => Double) extends Function0[Double] {
  def apply() = left() * right()
}
