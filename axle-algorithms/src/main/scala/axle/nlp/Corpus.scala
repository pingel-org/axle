package axle.nlp

import scala.collection.GenSeq

import axle.enrichGenSeq
import spire.implicits.LongAlgebra
import axle.Show

case class Corpus(val documents: GenSeq[String], language: Language) {

  lazy val wordCountMap: Map[String, Long] =
    documents.flatMap(doc => language.tokenize(doc.toLowerCase)).tally[Long]

  def wordCount(word: String): Option[Long] = wordCountMap.get(word)

  def topWordCounts(cutoff: Long): List[(String, Long)] =
    wordCountMap
      .filter { _._2 > cutoff }
      .toList
      .sortBy { _._2 }
      .reverse

  def topWords(cutoff: Long): List[String] =
    topWordCounts(cutoff) map { _._1 }

  lazy val bigramCounts = documents.flatMap({ d =>
    bigrams(language.tokenize(d.toLowerCase))
  }).tally[Long]

  def sortedBigramCounts: List[((String, String), Long)] =
    bigramCounts
      .filter { _._2 > 1 }
      .toList
      .sortBy { _._2 }
      .reverse

  def topBigrams(maxBigrams: Int): List[(String, String)] =
    sortedBigramCounts take (maxBigrams) map { _._1 }

}

object Corpus {

  implicit val showCorpus: Show[Corpus] = new Show[Corpus] {

    def text(corpus: Corpus): String = {

      import corpus._

      val wordCutoff = 20L

      s"""
Corpus of ${documents.length} documents.
There are ${topWords(wordCutoff).length} unique words used more than $wordCutoff time(s).
Top 10 words: ${topWords(wordCutoff).take(10).mkString(", ")}
Top 10 bigrams: ${topBigrams(10).mkString(", ")}
"""
    }
  }

}