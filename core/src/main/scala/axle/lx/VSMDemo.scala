package axle.lx

import axle._

object VSMDemo {

  val stopwords = Set("the", "a", "of", "for", "in").toSet // TODO extend this

  val corpus = List(
    "the quick brown fox jumps over the lazy dog",
    "fox jumps over dog eden was the sumerian word edine",
    "quick lazy word",
    "foo bar dog")

  val un = new UnweightedDocumentVectorSpace(stopwords, corpus)
  un.similarityMatrix()

  val tfidf = new TFIDFDocumentVectorSpace(stopwords, corpus)
  tfidf.similarityMatrix()

}
