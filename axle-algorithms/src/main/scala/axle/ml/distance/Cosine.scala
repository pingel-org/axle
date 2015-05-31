package axle.ml.distance

import scala.math.Pi
import scala.math.abs
import scala.math.acos

import spire.algebra.AdditiveAbGroup
import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.algebra.MetricSpace
import spire.algebra.NRoot
import spire.implicits.convertableOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.math.ConvertableFrom
import spire.math.ConvertableTo

/**
 *
 * Cosine
 *
 * http://en.wikipedia.org/wiki/Cosine_similarity
 *
 * @param n = num columns in row vectors
 *
 * http://math.stackexchange.com/questions/102924/cosine-similarity-distance-and-triangle-equation
 *
 */

case class Cosine[M, D: NRoot: Field: AdditiveAbGroup: ConvertableFrom: ConvertableTo](
  implicit ips: InnerProductSpace[M, D])
    extends MetricSpace[M, D] {

  val normed = ips.normed

  val norm = normed.norm _
  import ips.dot

  def similarity(v: M, w: M): D =
    dot(v, w) / (norm(v) * norm(w))

  def distance(v: M, w: M): D = {

    val d = 1d - abs(acos(similarity(v, w).toDouble) / Pi)

    ConvertableTo[D].fromDouble(d)
  }

}
