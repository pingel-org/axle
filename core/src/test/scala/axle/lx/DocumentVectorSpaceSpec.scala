package axle.lx

import org.specs2.mutable._

import axle.algebra._

class DocumentVectorSpaceSpec extends Specification {

  "dvs" should {
    "work" in {
      val stopwords = Set("the", "a", "of", "for", "in").toSet // TODO extend this

      val corpus = Vector(
        "the quick brown fox jumps over the lazy dog",
        "fox jumps over dog eden was the sumerian word edine",
        "hostname and fqdn for the instance can be critical if you're automating deployment with",
        "quick lazy word",
        "foo bar dog")

      val un = new UnweightedDocumentVectorSpace(stopwords, () => corpus.iterator)
      un.space().distanceMatrix(corpus.map(un.doc2vector(_)))

      val tfidf = new TFIDFDocumentVectorSpace(stopwords, () => corpus.iterator)
      tfidf.space().distanceMatrix(corpus.map(tfidf.doc2vector(_)))

      1 must be equalTo (1)
    }
  }

}