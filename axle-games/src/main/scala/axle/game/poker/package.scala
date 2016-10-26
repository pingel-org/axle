package axle.game

package object poker {

  lazy val moveParser = MoveParser()

  implicit val evState: State[Poker, PokerState, PokerOutcome, PokerMove] =
    new State[Poker, PokerState, PokerOutcome, PokerMove] {

      def applyMove(s: PokerState, game: Poker, move: PokerMove)(
        implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): PokerState =
        s(game, move)

      def displayTo(s: PokerState, observer: Player, game: Poker)(
        implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): String =
        s.displayTo(observer, game)

      def mover(s: PokerState): Option[Player] =
        s.moverOpt

      def moves(s: PokerState, game: Poker): Seq[PokerMove] =
        s.moves(game)

      def outcome(s: PokerState, game: Poker): Option[PokerOutcome] =
        s.outcome(game)
    }


}