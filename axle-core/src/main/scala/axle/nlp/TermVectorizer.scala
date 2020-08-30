package axle.nlp

//import spire.algebra.Ring
import spire.algebra.CRing
import spire.implicits.MapCRng
import spire.implicits.additiveSemigroupOps

case class TermVectorizer[V: CRing](stopwords: Set[String])
  extends Function1[String, Map[String, V]] {

  val one = CRing[V].one
  val zero = CRing[V].zero

  val whitespace = """\s+""".r

  val emptyCount = Map.empty[String, V].withDefaultValue(zero)

  def countWordsInLine(line: String): Map[String, V] = {
    val mapCR = new spire.std.MapCRng[String, V]()
    whitespace
      .split(line.toLowerCase)
      .filterNot(stopwords.contains)
      .aggregate(emptyCount)(
        (m, w) => m + (w -> (m(w) + one)),
        mapCR.plus
      )
  }

  def uniqueWordsInLine(line: String): Map[String, V] =
    whitespace.split(line.toLowerCase)
      .filterNot(stopwords.contains)
      .toSet
      .map((w: String) => (w, one))
      .toMap

  def wordCount(is: Seq[String]): Map[String, V] =
    is.aggregate(emptyCount)((m, line) => m + countWordsInLine(line), _ + _)

  def wordExistsCount(is: Seq[String]): Map[String, V] =
    is.aggregate(emptyCount)((m, line) => m + uniqueWordsInLine(line), _ + _)

  def apply(doc: String): Map[String, V] =
    wordCount(List(doc))

}
