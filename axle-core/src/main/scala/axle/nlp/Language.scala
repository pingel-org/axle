package axle.nlp

trait Language {

  def stopWords: Set[String]

  def tokenize(s: String): IndexedSeq[String]

  def stem(word: String): String

}
