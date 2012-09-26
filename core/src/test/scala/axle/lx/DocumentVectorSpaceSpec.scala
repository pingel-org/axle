package axle.lx

import org.specs2.mutable._

class DocumentVectorSpaceSpec extends Specification {

  "dvs" should {
    "work" in {
      val stopwords = Set("the", "a", "of", "for", "in").toSet // TODO extend this

      val corpus = List(
        "the quick brown fox jumps over the lazy dog",
        "fox jumps over dog eden was the sumerian word edine",
        "hostname and fqdn for the instance can be critical if you're automating deployment with",
        "quick lazy word",
        "foo bar dog")

      val un = new UnweightedDocumentVectorSpace(stopwords, corpus)
      // un.similarityMatrix()

      val tfidf = new TFIDFDocumentVectorSpace(stopwords, corpus)
      // tfidf.similarityMatrix()

      1 must be equalTo (1)
    }
  }

}