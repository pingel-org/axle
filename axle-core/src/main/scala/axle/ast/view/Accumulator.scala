package axle.ast.view

trait Accumulator {

  type A <: Accumulator

  def accRaw(s: String, n: Int): A

  def accNewline(): A

  def accSpace(): A

  def accSpaces(): A

  def accSpan(spanclass: String, s: String, n: Int): A

  def accPushStack(): A

  def accPopAndWrapStack(label: String): A

}
