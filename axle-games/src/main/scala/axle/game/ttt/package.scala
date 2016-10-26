package axle.game

package object ttt {

  implicit val evState: State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

      def applyMove(s: TicTacToeState, game: TicTacToe, move: TicTacToeMove)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): TicTacToeState = {
        val nextMoverOptFn = (newState: TicTacToeState) =>
          if (outcome(newState, game).isDefined) {
            None
          } else {
            Some(game.playerAfter(s.moverOpt.get))
          }
        game.state(nextMoverOptFn, s.place(move.position, s.moverOpt.get))
      }

      def displayTo(s: TicTacToeState, viewer: Player, game: TicTacToe)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): String =
        s.displayTo(viewer, game)

      def mover(s: TicTacToeState): Option[Player] =
        s.moverOpt

      def moves(s: TicTacToeState, game: TicTacToe): Seq[TicTacToeMove] =
        mover(s).map { p => s.openPositions(game).map(TicTacToeMove(_, game.boardSize)) } getOrElse (List.empty)

      def outcome(s: TicTacToeState, game: TicTacToe): Option[TicTacToeOutcome] =
        s.outcome(game)
    }


}