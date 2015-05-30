package axle.ml.distance

import scala.math.Pi
import scala.math.acos

import axle.algebra.LinearAlgebra
import axle.algebra.Zero
import axle.syntax.linearalgebra.matrixOps
import spire.algebra.Field
import spire.algebra.MetricSpace
import spire.algebra.MultiplicativeMonoid
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
 * distance(r1, r2) = 1.0 - abs(rvDot(r1, r2) / (norm(r1) * norm(r2)))
 *
 */

case class Cosine[M, R: Zero: MultiplicativeMonoid, C: Zero, D: Field: ConvertableFrom: ConvertableTo](n: C)(
  implicit la: LinearAlgebra[M, R, C, D])
    extends MetricSpace[M, D] {
  def dot(v: M, w: M): D =
    v.mulPointwise(w).rowSums.scalar

  def similarity(v: M, w: M): D =
    dot(v, w) / (dot(v, v) * dot(w, w))

  def distance(v: M, w: M): D = {
    // assert(v.isRowVector && w.isRowVector && v.length === w.length)
    val d = 1d - (acos(similarity(v, w).toDouble) / Pi)
    ConvertableTo[D].fromDouble(d)
  }

}
