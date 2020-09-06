package axle.nlp

import cats.Show
import cats.Monad
import cats.Order
import cats.Order.catsKernelOrderingForOrder
import cats.syntax.all._

import spire.algebra.CRing

import axle.algebra.Cephalate
import axle.algebra.Talliable
import axle.algebra.Zipper
import axle.syntax.talliable.talliableOps

case class Corpus[
  F[_]: Talliable: Zipper: Cephalate: Monad,
  N: Order](
  val documents: F[String],
  language: Language[F])(
  implicit val cRingN: CRing[N]
  ) {

  // implicit val ringLong: CRing[N] = spire.implicits.LongAlgebra

  lazy val wordCountMap: Map[String, N] =
    documents.flatMap(doc => language.tokenize(doc.toLowerCase)).tally

  def wordCount(word: String): Option[N] = wordCountMap.get(word)

  def topWordCounts(cutoff: N): List[(String, N)] =
    wordCountMap
      .filter { _._2 > cutoff }
      .toList
      .sortBy { _._2 }
      .reverse

  def wordsMoreFrequentThan(cutoff: N): List[String] =
    topWordCounts(cutoff) map { _._1 }

  def topKWords(k: Int): List[String] =
    wordCountMap.toList.sortBy(_._2).reverse.take(k).map(_._1)

  lazy val bigramCounts = documents.flatMap({ d =>
    bigrams(language.tokenize(d.toLowerCase))
  }).tally

  def sortedBigramCounts: List[((String, String), N)] =
    bigramCounts
      .filter { _._2 > CRing[N].one }
      .toList
      .sortBy { _._2 }
      .reverse

  def topKBigrams(k: Int): List[(String, String)] =
    sortedBigramCounts take (k) map { _._1 }

}

object Corpus {

  import spire.implicits.additiveSemigroupOps
  import spire.implicits.multiplicativeSemigroupOps
  import axle.algebra.Finite
  import axle.syntax.finite.finiteOps

  implicit def showCorpus[F[_], N](implicit fin: Finite[F, N]): Show[Corpus[F, N]] = corpus => {

    import corpus._

    val one = CRing[N].one
    val two = (one + one)
    val four = two * two
    val eight = four * two
    val sixteen = eight * two

    val wordCutoff = sixteen // NOTE: was 20

    s"""
Corpus of ${documents.size} documents.
There are ${wordsMoreFrequentThan(wordCutoff).length} unique words used more than $wordCutoff time(s).
Top 10 words: ${topKWords(10).mkString(", ")}
Top 10 bigrams: ${topKBigrams(10).mkString(", ")}
"""
  }

}
