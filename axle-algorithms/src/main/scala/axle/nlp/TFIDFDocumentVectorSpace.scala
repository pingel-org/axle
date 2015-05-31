package axle.nlp

import scala.math.log
import scala.reflect.ClassTag

import axle.algebra.Aggregatable
import axle.algebra.Σ
import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.convertableOps
import spire.implicits.multiplicativeSemigroupOps
import spire.math.ConvertableFrom
import spire.math.ConvertableTo

case class TFIDFDocumentVectorSpace[D: Field: ClassTag: ConvertableFrom: ConvertableTo](
  corpus: Iterable[String],
  termVectorizer: TermVectorizer[D])
    extends DocumentVectorSpace[D] {

  def scalar = Field[D]

  val dOne = Ring[D].one

  val numDocs = corpus.size

  val documentFrequency: Map[String, D] =
    termVectorizer.wordExistsCount(corpus.toList).withDefaultValue(dOne)

  private[this] def termWeight(term: String, tv: Map[String, D]): D = {

    val weight = log(numDocs.toDouble / documentFrequency(term).toDouble)

    ConvertableTo[D].fromDouble(weight) * tv(term)
  }

  def dot(v1: Map[String, D], v2: Map[String, D]): D = {

    val commonTerms = (v1.keySet intersect v2.keySet).toList

    val weights = commonTerms.map(term => scalar.times(termWeight(term, v1), termWeight(term, v2)))

    Σ(weights)(implicitly[ClassTag[D]], scalar, Aggregatable[List])
  }

}
