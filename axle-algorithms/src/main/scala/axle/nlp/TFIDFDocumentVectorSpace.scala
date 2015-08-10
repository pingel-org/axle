package axle.nlp

import spire.math.log

import axle.algebra.Aggregatable
import axle.algebra.Σ
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.convertableOps
import spire.implicits.multiplicativeSemigroupOps
import spire.math.ConvertableFrom
import spire.math.ConvertableTo

case class TFIDFDocumentVectorSpace[D: Field: ConvertableFrom: ConvertableTo](
  corpus: Iterable[String],
  termVectorizer: TermVectorizer[D])(implicit val eqD: Eq[D])
    extends DocumentVectorSpace[D] {

  def scalar = Field[D]

  val numDocs = corpus.size

  // TODO previously the default value was a "one",
  // which prevents division by zero in the termWeight calculation
  // but causes several axioms to fail

  val documentFrequency: Map[String, D] =
    termVectorizer.wordExistsCount(corpus.toList).withDefaultValue(dZero)

  private[this] def termWeight(term: String, tv: Map[String, D]): D = {

    val weight = log(numDocs.toDouble / documentFrequency(term).toDouble)

    ConvertableTo[D].fromDouble(weight) * tv(term)
  }

  def dot(v1: Map[String, D], v2: Map[String, D]): D = {

    val commonTerms = (v1.keySet intersect v2.keySet).toList

    val weights = commonTerms.map(term => scalar.times(termWeight(term, v1), termWeight(term, v2)))

    Σ(weights)(scalar, Aggregatable[List[D], D, D])
  }

}
