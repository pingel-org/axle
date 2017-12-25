package axle.ml

trait Normalize[M] extends (Seq[Double] => M) {

  def normalizedData: M

  def apply(featureList: Seq[Double]): M

  def unapply(featureRow: M): Seq[Double]

  //  def random(): M
}