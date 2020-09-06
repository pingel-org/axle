package axle.nlp

trait Language[F[_]] {

  def stopWords: Set[String]

  def tokenize(s: String): F[String]

  def stem(word: String): String

}
