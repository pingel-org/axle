package axle.nlp

import axle._
import spire.implicits._

trait Corpus[D <: Document] {

  def documents(): collection.GenSeq[D]

  lazy val wordCountMap = documents flatMap (_.tokens) countMap

  def topWordCounts(cutoff: Long) =
    wordCountMap
      .filter { case (_, n: Long) => n > cutoff }
      .toList
      .sortBy { case (_, n: Long) => n }
      .reverse

  def topWords(cutoff: Long) = topWordCounts(cutoff) map { case (word: String, _) => word }

  override def toString(): String = {

    val wordCutoff: Long = 20

    s"""
Corpus of ${documents.length} documents.
There are ${topWords(wordCutoff).length} unique words used more than $wordCutoff time(s).
Top 10 words: ${topWords(wordCutoff).take(10).mkString(", ")}
Top 10 bigrams: ${topBigrams(10).mkString(", ")}
"""
  }

  lazy val bigramCounts = documents.flatMap((d: D) => d.tokens.zip(d.tokens.tail)).countMap

  def sortedBigramCounts() =
    bigramCounts
      .filter { case (_, n: Long) => n > 1 }
      .toList
      .sortBy { case (bg: (String, String), n: Long) => n }
      .reverse

  def topBigrams(maxBigrams: Int) = sortedBigramCounts.take(maxBigrams).map(_._1)

}
