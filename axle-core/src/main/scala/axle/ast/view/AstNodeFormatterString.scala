package axle.ast.view

class AstNodeFormatterString(
  config: FormatterConfig,
  state: FormatterState,
  subState: List[String]) // was "tokens"
  extends AstNodeFormatter[String, List[String]](config, state, subState) {

  def apply(s: FormatterState, ss: List[String]): AstNodeFormatterString =
    new AstNodeFormatterString(config, s, ss)

  override def result: String = subState.mkString("")

  override def toString: String = subState.toList.mkString("")

  def accRaw(s: String, n: Int): AstNodeFormatterString =
    new AstNodeFormatterString(config, state, subState ++ List(s))

  def accNewline: AstNodeFormatterString =
    new AstNodeFormatterString(config, state, subState ++ List("\n"))

  def accSpace: AstNodeFormatterString =
    new AstNodeFormatterString(config, state, subState ++ List(" "))

  def accSpaces: AstNodeFormatterString =
    new AstNodeFormatterString(config, state, subState ++ List("   "))

  def accSpan(spanclass: String, s: String, n: Int): AstNodeFormatterString =
    new AstNodeFormatterString(config, state, subState ++ List(s))

  def accPushStack: AstNodeFormatter[String, List[String]] = this

  def accPopAndWrapStack(label: String): AstNodeFormatter[String, List[String]] = this

}
