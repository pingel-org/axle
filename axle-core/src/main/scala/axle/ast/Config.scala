
package axle.ast

object Config {
  // def corpus_root = "corpus/2.6"
  def corpus_root: String = "corpus/small"
  // def corpus_root = "corpus/dev"
  def maxfiles: Int = 30 // 4 // 10 // 1500 // 1500
  def indexfile: String = "TODO"
  def treedepth: Int = 5
  val htmlDirectory: String = "html"
}
