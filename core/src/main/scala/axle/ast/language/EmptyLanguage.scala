
package axle.ast.language

import axle.ast._
import scala.util.matching.Regex

object EmptyLanguage {

  val lang = new Language(
    "empty",
    new Rule("EmptyNode", Nop()) :: Nil,
    Nil,
    (text: String) => None,
    ast => ast)

  def language = lang

}
