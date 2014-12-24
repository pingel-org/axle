package axle.ast.view

import axle.Show

case class AstNodeFormatterString(
  config: FormatterConfig,
  state: FormatterState,
  subState: List[String]) // was "tokens"
  extends AstNodeFormatter[String, List[String]] {

  def apply(s: FormatterState, ss: List[String]): AstNodeFormatterString =
    AstNodeFormatterString(config, s, ss)

  override def result: String = subState.mkString("")

  def accRaw(s: String, n: Int): AstNodeFormatterString =
    AstNodeFormatterString(config, state, subState ++ List(s))

  def accNewline: AstNodeFormatterString =
    AstNodeFormatterString(config, state, subState ++ List("\n"))

  def accSpace: AstNodeFormatterString =
    AstNodeFormatterString(config, state, subState ++ List(" "))

  def accSpaces: AstNodeFormatterString =
    AstNodeFormatterString(config, state, subState ++ List("   "))

  def accSpan(spanclass: String, s: String, n: Int): AstNodeFormatterString =
    AstNodeFormatterString(config, state, subState ++ List(s))

  def accPushStack: AstNodeFormatter[String, List[String]] = this

  def accPopAndWrapStack(label: String): AstNodeFormatter[String, List[String]] = this

}

object AstNodeFormatterString {

  implicit val showAstNodeFormatterString: Show[AstNodeFormatterString] =
    new Show[AstNodeFormatterString] {

      def text(anfs: AstNodeFormatterString): String = anfs.subState.toList.mkString("")
    }

}
