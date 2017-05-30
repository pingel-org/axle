package axle.game.poker

import util.parsing.combinator._

case class MoveParser() extends RegexParsers {

  override val skipWhitespace = true

  lazy val NUMBER = "\\d+".r

  def raise: Parser[Raise] = ("r(aise)?".r ~ NUMBER) ^^ { case r ~ n => Raise(n.toInt) }

  def call: Parser[Call] = "c(all)?".r ^^ { case c => Call() }

  def fold: Parser[Fold] = "f(old)?".r ^^ { case f => Fold() }

  def move: Parser[PokerMove] = raise | call | fold

  def parse(input: String): Either[String, PokerMove] = {
    val parsed = parseAll(move, input)
    parsed.map(Right.apply).getOrElse(Left("invalid input: " + input))
  }

}
