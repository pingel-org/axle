package axle.algebra.distance

import spire.algebra.Field
import spire.algebra.InnerProductSpace
import spire.algebra.NRoot
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps

/**
 * Cosine
 *
 * http://en.wikipedia.org/wiki/Cosine_similarity
 *
 * n = num columns in row vectors
 *
 * http://math.stackexchange.com/questions/102924/cosine-similarity-distance-and-triangle-equation
 *
 * Also see:
 *
 *   Metric Distances Derived from Cosine Similarity and Pearson and Spearman Correlations
 *   http://arxiv.org/pdf/1208.3145.pdf
 *
 * one distance in terms of similarity:
 *
 *   1d - 2d *abs(acos(similarity(u, v)) / Pi)
 */

class Cosine[M, D: NRoot: Field](
  implicit
  ips: InnerProductSpace[M, D]) {

  val normed = ips.normed

  val norm = normed.norm _
  import ips.dot

  def distance(v: M, w: M): D =
    dot(v, w) / (norm(v) * norm(w))

}
