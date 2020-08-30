package axle.nlp

import scala.collection.GenSeq

import cats.Show
import spire.algebra.CRing
import axle.enrichGenSeq

case class Corpus(val documents: GenSeq[String], language: Language) {

  implicit val ringLong: CRing[Long] = spire.implicits.LongAlgebra

  lazy val wordCountMap: Map[String, Long] =
    documents.flatMap(doc => language.tokenize(doc.toLowerCase)).tally[Long]

  def wordCount(word: String): Option[Long] = wordCountMap.get(word)

  def topWordCounts(cutoff: Long): List[(String, Long)] =
    wordCountMap
      .filter { _._2 > cutoff }
      .toList
      .sortBy { _._2 }
      .reverse

  def wordsMoreFrequentThan(cutoff: Long): List[String] =
    topWordCounts(cutoff) map { _._1 }

  def topKWords(k: Int): List[String] =
    wordCountMap.toList.sortBy(_._2).reverse.take(k).map(_._1)

  lazy val bigramCounts = documents.flatMap({ d =>
    bigrams(language.tokenize(d.toLowerCase))
  }).tally[Long]

  def sortedBigramCounts: List[((String, String), Long)] =
    bigramCounts
      .filter { _._2 > 1 }
      .toList
      .sortBy { _._2 }
      .reverse

  def topKBigrams(k: Int): List[(String, String)] =
    sortedBigramCounts take (k) map { _._1 }

}

object Corpus {

  implicit val showCorpus: Show[Corpus] = corpus => {

    import corpus._

    val wordCutoff = 20L

    s"""
Corpus of ${documents.length} documents.
There are ${wordsMoreFrequentThan(wordCutoff).length} unique words used more than $wordCutoff time(s).
Top 10 words: ${topKWords(10).mkString(", ")}
Top 10 bigrams: ${topKBigrams(10).mkString(", ")}
"""
  }

}
