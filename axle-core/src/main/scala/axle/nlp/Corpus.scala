package axle.nlp

import axle._
import spire.implicits._

trait Corpus[D <: Document] {

  def documents(): collection.GenSeq[D]

  val wordCutoff = 20
  val maxBigrams = 200

  val topWordCounts =
    documents.flatMap(_.tokens).countMap()
      .toList.sortBy(_._2).reverse
      .filter(_._2 > wordCutoff)
  // .take(maxWords)

  val topWords = topWordCounts.map(_._1)

  override def toString(): String = {
    "Corpus of " + documents.length + " documents.\n" +
      "There are " + topWords.length + " unique words used more than " + wordCutoff + " time(s).\n" +
      "Top 10 words: " + topWords.take(10).mkString(", ") + "\n" +
      "Top 10 bigrams: " + topBigrams.take(10).mkString(", ")
  }

  lazy val topBigramCounts =
    documents.flatMap((d: D) => d.tokens.zip(d.tokens.tail)).countMap()
      .toList.sortBy(_._2).reverse
      .filter(_._2 > 1)
      .take(maxBigrams)

  lazy val topBigrams = topBigramCounts.map(_._1)

}
