package axle.game.poker

import util.parsing.combinator._
import axle.game.Player

case class MoveParser() extends RegexParsers {

  override val skipWhitespace = true

  lazy val NUMBER = "\\d+".r

  def raise(player: Player): Parser[Raise] = ("r(aise)?".r ~ NUMBER) ^^ { case r ~ n => Raise(player, n.toInt) }

  def call(player: Player): Parser[Call] = "c(all)?".r ^^ { case c => Call(player) }

  def fold(player: Player): Parser[Fold] = "f(old)?".r ^^ { case f => Fold(player) }

  def move(player: Player): Parser[PokerMove] = raise(player) | call(player) | fold(player)

  def parse(input: String)(player: Player): Option[PokerMove] = parseAll(move(player), input).map(Some(_)).getOrElse(None)

}
