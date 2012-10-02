package axle.stats

case class PMultiply(left: Probability, right: Double) extends Probability {
  
  def apply() = left() * right
  
  def bayes() = null.asInstanceOf[() => Double]
}
