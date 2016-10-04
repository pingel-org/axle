package axle

package object game {

  def scriptToLastMoveState[G <: Game[G]](game: G, moves: List[G#MOVE]): (G#MOVE, G#STATE) =
    game.scriptedMoveStateStream(game.startState, moves.iterator).last

}