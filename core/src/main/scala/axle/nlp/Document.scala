package axle.nlp

import axle.ScalaMapReduce._

class Document(text: String) {

  lazy val tokens = language.English.tokenize(text)
  
  lazy val bigrams = tokens.zip(tokens.tail)

  lazy val wordCounts = count(List(this).iterator, (d: Document) => d.tokens)
  lazy val bigramCounts = count(List(this).iterator, (d: Document) => d.bigrams)

  lazy val averageWordLength = tokens.map(_.length).sum / tokens.length.toDouble

  def features[D <: Document](corpus: Corpus[D]): IndexedSeq[Double] =
    corpus.topWords.map(wordCounts.get(_).getOrElse(0).toDouble).toIndexedSeq ++ Vector(averageWordLength)

  def numFullFeatures(corpus: Corpus[Document]) = corpus.topWords.length + 1

}
