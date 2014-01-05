package axle.nlp

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

      val unweighted = UnweightedDocumentVectorSpace(stopwords, corpus)
      unweighted.space.distanceMatrix(corpus.map(unweighted.doc2vector))

      val tfidf = TFIDFDocumentVectorSpace(stopwords, corpus)
      tfidf.space.distanceMatrix(corpus.map(tfidf.doc2vector))

      1 must be equalTo (1)
    }

    "work again" in {

      val lines = Vector("foo bar baz", "foo fu", "fu fu fu bar")
      val dvs = UnweightedDocumentVectorSpace(Set("this", "the"), lines)

      dvs.wordCount(lines) must be equalTo Map("foo" -> 2, "bar" -> 2, "baz" -> 1, "fu" -> 4)
      dvs.wordExistsCount(lines) must be equalTo Map("foo" -> 2, "bar" -> 2, "baz" -> 1, "fu" -> 2)
    }
  }

}
