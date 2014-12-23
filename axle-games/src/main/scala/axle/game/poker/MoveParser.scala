package axle.game.poker

import util.parsing.combinator._

case class MoveParser() extends RegexParsers {

  override val skipWhitespace = true

  lazy val NUMBER = "\\d+".r

  def raise(implicit player: PokerPlayer, game: Poker): Parser[Raise] = ("r(aise)?".r ~ NUMBER) ^^ { case r ~ n => Raise(player, n.toInt) }
  
  def call(implicit player: PokerPlayer, game: Poker): Parser[Call] = "c(all)?".r ^^ { case c => Call(player) }
  
  def fold(implicit player: PokerPlayer, game: Poker): Parser[Fold] = "f(old)?".r ^^ { case f => Fold(player) }

  def move(implicit player: PokerPlayer, game: Poker): Parser[PokerMove] = (raise | call | fold)

  def parse(input: String)(implicit player: PokerPlayer, game: Poker): Option[PokerMove] = parseAll(move, input).map(Some(_)).getOrElse(None)

}
