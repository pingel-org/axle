package axle.lx.language

object English {

  // From Lucene's list of stopwords:

  val stopWords = Set(
    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "if", "in", "into", "is", "it",
    "no", "not", "of", "on", "or", "such",
    "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with"
  )

  def tokenize(s: String): IndexedSeq[String] = s
    .replaceAll("""([\?!()\";\|\[\]\.\,'])""", " ")
    .trim
    .split("\\s+")
    .toIndexedSeq

  lazy val stemmer = new org.tartarus.snowball.ext.englishStemmer()

  // TODO serialize access to this method:
  def stem(word: String): String = {
    stemmer.setCurrent(word)
    stemmer.stem
    stemmer.getCurrent
  }

}