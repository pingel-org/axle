package axle.nlp

import axle._
import spire.implicits._

class Document(text: String) {

  lazy val tokens = language.English.tokenize(text)

  lazy val bigrams = tokens.sliding(2).toVector
  
  lazy val wordCounts = tokens.countMap()
  
  lazy val bigramCounts = bigrams.countMap()

  lazy val averageWordLength = tokens.map(_.length).sum / tokens.length.toDouble

  def features[D <: Document](corpus: Corpus[D]): IndexedSeq[Double] =
    corpus.topWords.map(wordCounts.get(_).getOrElse(0L).toDouble).toIndexedSeq ++ Vector(averageWordLength)

  def numFullFeatures(corpus: Corpus[Document]) = corpus.topWords.length + 1

}
