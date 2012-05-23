
package axle.game

trait State[GAME <: Game] {

  def player(): Player[GAME]

  def applyMove(move: Move[GAME]): State[GAME]

  def isTerminal(): Boolean

  def getOutcome(): Outcome[GAME]

}
