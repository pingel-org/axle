package axle.ml.distance

import scala.math.Pi
import scala.math.abs
import scala.math.acos

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
 * Also see:
 *
 *   "Metric Distances Derived from Cosine Similarity and Pearson and Spearman Correlations"
 *   http://arxiv.org/pdf/1208.3145.pdf
 *
 * one distance in terms of similarity:
 *
 *   1d - 2d *abs(acos(similarity(u, v)) / Pi)
 *
 */

case class Cosine[M, D: NRoot: Field: ConvertableFrom: ConvertableTo](
  implicit ips: InnerProductSpace[M, D]) {

  val normed = ips.normed

  val norm = normed.norm _
  import ips.dot

  def distance(v: M, w: M): D =
    dot(v, w) / (norm(v) * norm(w))

}
