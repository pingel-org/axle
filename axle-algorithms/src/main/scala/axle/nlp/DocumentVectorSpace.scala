package axle.nlp

import spire.algebra.InnerProductSpace
import spire.algebra.Ring
import spire.implicits.multiplicativeSemigroupOps

trait DocumentVectorSpace[D]
    extends InnerProductSpace[Map[String, D], D] {

  lazy val dZero = scalar.zero

  def negate(x: Map[String, D]): Map[String, D] =
    x.map(kv => (kv._1, scalar.negate(kv._2)))

  def zero: Map[String, D] =
    Map.empty

  def plus(x: Map[String, D], y: Map[String, D]): Map[String, D] =
    (x.keySet union y.keySet).toIterable.map(k =>
      (k, scalar.plus(x.get(k).getOrElse(dZero), y.get(k).getOrElse(dZero)))).toMap

  def timesl(r: D, v: Map[String, D]): Map[String, D] =
    v.map(kv => (kv._1, kv._2 * r))

}
