package axle.game

import cats.implicits._

package object ttt {

  // board: Array[Option[Player]], boardSize: Int

  def numPositions(board: Array[Option[Player]]): Int =
    board.length

  def row(board: Array[Option[Player]], boardSize: Int, r: Int): IndexedSeq[Option[Player]] =
    (0 until boardSize) map { c => playerAt(board, boardSize, r, c) }

  def column(board: Array[Option[Player]], boardSize: Int, c: Int): IndexedSeq[Option[Player]] =
    (0 until boardSize) map { r => playerAt(board, boardSize, r, c) }

  def playerAt(board: Array[Option[Player]], boardSize: Int, r: Int, c: Int): Option[Player] =
    board(c + r * boardSize)

  def playerAtIndex(board: Array[Option[Player]], boardSize: Int, i: Int): Option[Player] =
    board(i - 1)

  def place(board: Array[Option[Player]], position: Int, player: Player): Array[Option[Player]] = {
    val updated = board.clone()
    updated.update(position - 1, Option(player))
    updated
  }

  def hasWonRow(board: Array[Option[Player]], boardSize: Int, player: Player): Boolean =
    (0 until boardSize).exists(row(board, boardSize, _).toList.forall(_ == Option(player)))

  def hasWonColumn(board: Array[Option[Player]], boardSize: Int, player: Player): Boolean =
    (0 until boardSize).exists(column(board, boardSize, _).toList.forall(_ == Option(player)))

  def hasWonDiagonal(board: Array[Option[Player]], boardSize: Int, player: Player): Boolean =
    (0 until boardSize).forall(i => playerAt(board, boardSize, i, i) == Option(player)) ||
      (0 until boardSize).forall(i => playerAt(board, boardSize, i, (boardSize - 1) - i) == Option(player))

  def hasWon(board: Array[Option[Player]], boardSize: Int, player: Player): Boolean =
    hasWonRow(board, boardSize, player) ||
      hasWonColumn(board, boardSize, player) ||
        hasWonDiagonal(board, boardSize, player)

  def openPositions(board: Array[Option[Player]], boardSize: Int, ttt: TicTacToe): IndexedSeq[Int] =
    (1 to board.length).filter(playerAtIndex(board, boardSize, _).isEmpty)

  implicit val eqMove = cats.kernel.Eq.fromUniversalEquals[TicTacToeMove]

  implicit val evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove, TicTacToeState, TicTacToeMove] =
    new Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove, TicTacToeState, TicTacToeMove] {

      def startState(ttt: TicTacToe): TicTacToeState =
        TicTacToeState(Option(ttt.x), ttt.startBoard, ttt.boardSize)

      def startFrom(ttt: TicTacToe, s: TicTacToeState): Option[TicTacToeState] =
        Option(startState(ttt))

      def players(g: TicTacToe): IndexedSeq[Player] =
        g.players

      def isValid(g: TicTacToe, state: TicTacToeState, move: TicTacToeMove): Either[String, TicTacToeMove] =
        if (playerAtIndex(state.board, state.boardSize, move.position).isEmpty) {
          Right(move)
        } else {
          Left("That space is occupied.")
        }

      def applyMove(game: TicTacToe, state: TicTacToeState, move: TicTacToeMove): TicTacToeState = {
        val mover = state.moverOpt.get
        val newBoard = place(state.board, move.position, mover)
        val nextMoverOpt =
          if( hasWon(newBoard, game.boardSize, mover) ) {
            None
          } else {
            Option(game.playerAfter(mover))
          }
        TicTacToeState(nextMoverOpt, newBoard, game.boardSize)
      }

      def mover(game: TicTacToe, state: TicTacToeState): Either[TicTacToeOutcome, Player] = {
        import state._
        val winner = game.players.find(p => hasWon(board, boardSize, p))
        if (winner.isDefined) {
          Left(TicTacToeOutcome(winner))
        } else if (openPositions(board, boardSize, game).length === 0) {
          Left(TicTacToeOutcome(None))
        } else {
          Right(state.moverOpt.get)
        }
      }

      def moves(game: TicTacToe, s: TicTacToeState): Seq[TicTacToeMove] =
        mover(game, s).map { p => openPositions(s.board, s.boardSize, game).map(TicTacToeMove(_, game.boardSize)) } getOrElse (List.empty)

      def maskState(game: TicTacToe, state: TicTacToeState, observer: Player): TicTacToeState =
        state

      def maskMove(game: TicTacToe, move: TicTacToeMove, mover: Player, observer: Player): TicTacToeMove =
        move

    }

  implicit val evGameIO: GameIO[TicTacToe, TicTacToeOutcome, TicTacToeMove, TicTacToeState, TicTacToeMove] =
    new GameIO[TicTacToe, TicTacToeOutcome, TicTacToeMove, TicTacToeState, TicTacToeMove] {

      def parseMove(g: TicTacToe, input: String): Either[String, TicTacToeMove] = {
        val eitherI: Either[String, Int] = try {
          val position = input.toInt
          if (position >= 1 && position <= g.numPositions) {
            Right(position)
          } else {
            Left("Please enter a number between 1 and " + g.numPositions)
          }
        } catch {
          case e: Exception => {
            Left(input + " is not a valid move.  Please select again")
          }
        }
        eitherI.map { position =>
          TicTacToeMove(position, g.boardSize)
        }
      }

      def introMessage(ttt: TicTacToe) = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)

      def displayStateTo(game: TicTacToe, s: TicTacToeState, observer: Player): String = {
        // val keyWidth = string(s.numPositions).length

        "Board:         Movement Key:\n" +
          0.until(s.boardSize).map(r => {
            row(s.board, s.boardSize, r).map(playerOpt => playerOpt.map(game.markFor).getOrElse(" ")).mkString("|") +
              "          " +
              (1 + r * s.boardSize).until(1 + (r + 1) * s.boardSize).mkString("|") // TODO rjust(keyWidth)
          }).mkString("\n")
      }

      def displayMoveTo(
        game:     TicTacToe,
        move:     TicTacToeMove,
        mover:    Player,
        observer: Player): String =
        mover.referenceFor(observer) +
          " put an " + game.markFor(mover) +
          " in the " + move.description + "."

      def displayOutcomeTo(
        game:     TicTacToe,
        outcome:  TicTacToeOutcome,
        observer: Player): String =
        outcome.winner map { wp =>
          s"${wp.referenceFor(observer)} beat " + evGame.players(game).filterNot(_ === wp).map(_.referenceFor(observer)).toList.mkString(" and ") + "!"
        } getOrElse ("The game was a draw.")

    }

}
