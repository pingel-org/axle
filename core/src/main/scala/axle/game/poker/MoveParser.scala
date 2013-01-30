package axle.game.poker

import util.parsing.combinator._

class MoveParser(player: PokerPlayer)(implicit game: Poker) extends RegexParsers {

  override val skipWhitespace = true

  lazy val NUMBER = "\\d+".r

  lazy val raise: Parser[Raise] = (("r" | "raise") ~ NUMBER) ^^ { case r ~ n => Raise(player, n.toInt) }
  lazy val call: Parser[Call] = ("c" | "call" | "check") ^^ { case c => Call(player) }
  lazy val fold: Parser[Fold] = ("f" | "fold") ^^ { case f => Fold(player) }

  lazy val move: Parser[PokerMove] = (raise | call | fold)

  def parse(input: String): Option[PokerMove] = parseAll(move, input).map(Some(_)).getOrElse(None)

}
