package axle.nlp

import spire.algebra.Field
import spire.algebra.Eq
import spire.implicits.multiplicativeSemigroupOps
import axle.math._

case class UnweightedDocumentVectorSpace[D: Field]()(implicit _eqD: Eq[D])
    extends DocumentVectorSpace[D] {

  def scalar = Field[D]

  implicit def eqD = _eqD

  def dot(v1: Map[String, D], v2: Map[String, D]): D = {

    val common = (v1.keySet intersect v2.keySet).toList

    Σ(common.map(w => v1(w) * v2(w)))
  }

}
