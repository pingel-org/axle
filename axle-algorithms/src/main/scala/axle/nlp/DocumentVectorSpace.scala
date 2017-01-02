package axle.nlp

import spire.algebra.InnerProductSpace
import spire.algebra.Eq
import spire.implicits.multiplicativeSemigroupOps

trait DocumentVectorSpace[D]
    extends InnerProductSpace[Map[String, D], D] {

  lazy val dZero = scalar.zero

  implicit def eqD: Eq[D]

  def negate(x: Map[String, D]): Map[String, D] =
    x.map(kv => (kv._1, scalar.negate(kv._2)))

  def zero: Map[String, D] =
    Map.empty

  def plus(x: Map[String, D], y: Map[String, D]): Map[String, D] = {
    val common = (x.keySet union y.keySet).toList
    common.flatMap(k => {
      val v = scalar.plus(x.get(k).getOrElse(dZero), y.get(k).getOrElse(dZero))
      if (scalar.isZero(v)) {
        None
      } else {
        Some(k -> v)
      }
    }).toMap
  }

  def timesl(r: D, v: Map[String, D]): Map[String, D] =
    v.map(kv => (kv._1, kv._2 * r))

}
