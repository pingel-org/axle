package org.pingel.bayes;

// this is read "X is independent of Y given Z"

class Independence(X: Set[RandomVariable], Z: Set[RandomVariable], Y: Set[RandomVariable]) {

  def variablesToString(s: Set[RandomVariable]): String = "{" + (for (v <- s) yield v.getName()).mkString("") + "}"

  override def toString(): String = {
    "I(" +
      variablesToString(X) +
      ", " +
      variablesToString(Z) +
      ", " +
      variablesToString(Y) +
      ")"
  }

}
