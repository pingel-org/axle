package axle.nlp

import spire.algebra._
import spire.math._
import spire.implicits._

trait DocumentVectorSpace
    extends InnerProductSpace[Map[String, Int], Double] {

  def negate(x: Map[String, Int]): Map[String, Int] = x.map(kv => (kv._1, -1 * kv._2))

  def zero: Map[String, Int] = Map.empty

  def plus(x: Map[String, Int], y: Map[String, Int]): Map[String, Int] =
    (x.keySet union y.keySet).toIterable.map(k => (k, x.get(k).getOrElse(0) + y.get(k).getOrElse(0))).toMap

  def timesl(r: Double, v: Map[String, Int]): Map[String, Int] = v.map(kv => (kv._1, (kv._2 * r).toInt))

  def scalar: Field[Double] = DoubleAlgebra

}
