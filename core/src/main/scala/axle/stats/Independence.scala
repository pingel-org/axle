package axle.stats

// this is read "X is independent of Y given Z"

class Independence(X: Set[RandomVariable[_]], Z: Set[RandomVariable[_]], Y: Set[RandomVariable[_]]) {

  def variablesToString(s: Set[RandomVariable[_]]): String = "{" + (for (v <- s) yield v.getName()).mkString("") + "}"

  override def toString(): String =
    "I(" + variablesToString(X) + ", " + variablesToString(Z) + ", " + variablesToString(Y) + ")"

}
