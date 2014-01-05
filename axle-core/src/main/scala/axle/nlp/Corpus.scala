package axle.nlp

import axle._
import spire.implicits._
import collection.GenSeq

class Corpus[T: Document](documents: GenSeq[T]) {

  val doc = implicitly[Document[T]]

  lazy val wordCountMap: Map[String, Long] = documents flatMap (doc.tokens) tally

  def wordCount(word: String): Option[Long] = wordCountMap.get(word)

  def topWordCounts(cutoff: Long): List[(String, Long)] =
    wordCountMap
      .filter { case (_, n: Long) => n > cutoff }
      .toList
      .sortBy { case (_, n: Long) => n }
      .reverse

  def topWords(cutoff: Long): List[String] = topWordCounts(cutoff) map { case (word: String, _) => word }

  override def toString: String = {

    val wordCutoff = 20L

    s"""
Corpus of ${documents.length} documents.
There are ${topWords(wordCutoff).length} unique words used more than $wordCutoff time(s).
Top 10 words: ${topWords(wordCutoff).take(10).mkString(", ")}
Top 10 bigrams: ${topBigrams(10).mkString(", ")}
"""
  }

  lazy val bigramCounts = documents.flatMap((d: T) => {
    val tokens = doc.tokens(d)
    tokens.zip(tokens.tail)
  }) tally

  def sortedBigramCounts: List[((String, String), Long)] =
    bigramCounts
      .filter { case (_, n: Long) => n > 1 }
      .toList
      .sortBy { case (bg: (String, String), n: Long) => n }
      .reverse

  def topBigrams(maxBigrams: Int): List[(String, String)] =
    sortedBigramCounts take (maxBigrams) map { case (bigram: (String, String), _) => bigram }

}
