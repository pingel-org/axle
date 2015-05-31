package axle.ml.distance

import scala.math.Pi
import scala.math.acos

import axle.algebra.LinearAlgebra
import axle.algebra.Zero
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.AdditiveAbGroup
import spire.algebra.Field
import spire.algebra.MetricSpace
import spire.algebra.NormedVectorSpace
import spire.algebra.InnerProductSpace
import spire.algebra.MultiplicativeMonoid
import spire.algebra.NRoot
import spire.implicits.convertableOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.nrootOps
import spire.implicits._
import math.abs
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
 * distance(r1, r2) = 1.0 - abs(rvDot(r1, r2) / (norm(r1) * norm(r2)))
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
    // val d = Field[D].one - similarity(v, w)
    // val d = 1d - (2d * (acos(similarity(v, w).toDouble) / Pi))
    val d = 1d - abs(acos(similarity(v, w).toDouble) / Pi)
    ConvertableTo[D].fromDouble(d)
  }

}
