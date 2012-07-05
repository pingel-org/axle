package axle.ast.language

import collection._
import axle.ast._

object LLTest2 {

  val grammar = new ParseTableGrammarBuilder("LLTest2").
    nt("F").
    t("a").t("b").t("c").t("d").
    t("(").t(")").
    t("+").t("-").t("*").t("/").
    r("1", "S", "F").
    r("2", "S", List("(", "S", "+", "F", ")")).
    r("3", "S", List("(", "S", "-", "F", ")")).
    r("4", "S", List("(", "S", "*", "F", ")")).
    r("5", "S", List("(", "S", "/", "F", ")")).
    r("6", "F", "a").
    r("7", "F", "b").
    r("8", "F", "c").
    r("9", "F", "d").
    build

}
