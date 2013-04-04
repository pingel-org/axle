package axle.nlp

import axle.ScalaMapReduce._

trait Corpus[D <: Document] {

  def documents(): IndexedSeq[D]

  val wordCutoff = 20
  val maxBigrams = 200

  val topWordCounts =
    count(documents.iterator, (d: D) => d.tokens)
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
    count(documents.iterator, (d: D) => d.tokens.zip(d.tokens.tail))
      .toList.sortBy(_._2).reverse
      .filter(_._2 > 1)
      .take(maxBigrams)

  lazy val topBigrams = topBigramCounts.map(_._1)

}
