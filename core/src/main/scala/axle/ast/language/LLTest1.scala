
package axle.ast.language

import scala.collection.mutable.Stack
import scala.collection.mutable.Map
import axle.Loggable
import axle.ast._

object LLTest1 extends Loggable {

  //		 parseTable += (Start, terminals("(")) -> rules("2")
  //		 parseTable += (Start, terminals("a")) -> rules("1")
  //		 parseTable += (nonTerminals("F"), terminals("a")) -> rules("3")

  val grammar = new ParseTableGrammarBuilder("LLTest1").
    nt("F").
    t("a").t("(").t(")").t("+").
    r("1", "S", "F").
    r("2", "S", List("(", "S", "+", "F", ")")).
    r("3", "F", "a").
    build
  /*
	val parse = new Parse(grammar, "S", "(a+a)")
	parse.parse

	info(grammar)
	info
	info("Parser Actions:")
	var replay = new Parse(grammar, "S", "(a+a)")
	for( action <- parse.derivation ) {
	  info(action)
	  replay.apply(action)
	  info(replay)
	}
 */

}
