package axle

import scala.Stream.cons

package object game {

  def moveStateStream[G, S, O, M](
    game: G,
    fromState: S)(
      implicit evGame: Game[G, S, O, M]): Stream[(S, M, S)] =
    evGame.mover(fromState).map(mover => {
      val strategy = evGame.strategyFor(game, mover)
      val move = strategy.apply(fromState, game)
      val toState = evGame.applyMove(fromState, game, move)
      cons((fromState, move, toState), moveStateStream(game, toState))
    }) getOrElse {
      Stream.empty
    }

  def play[G, S, O, M](game: G)(
    implicit evGame: Game[G, S, O, M]): S =
    play(game, evGame.startState(game), true)

  def play[G, S, O, M](
    game: G,
    start: S,
    intro: Boolean = true)(
      implicit evGame: Game[G, S, O, M]): S = {

    evGame.players(game) foreach { observer =>
      val display = evGame.displayerFor(game, observer)
      if (intro) {
        display(evGame.introMessage(game))
      }
      display(evGame.displayTo(start, observer, game))
    }

    val lastState = moveStateStream(game, start) map {
      case (fromState, move, toState) => {
        evGame.mover(fromState) foreach { mover =>
          evGame.players(game) foreach { observer =>
            val display = evGame.displayerFor(game, observer)
            display(evGame.displayMoveTo(game, mover, move, observer))
            display(evGame.displayTo(toState, observer, game))
          }
        }
        toState
      }
    } last

    evGame.players(game) foreach { observer =>
      val display = evGame.displayerFor(game, observer)
      display("")
      display(evGame.displayTo(lastState, observer, game))
      evGame.outcome(lastState, game) foreach { outcome =>
        display(evGame.displayOutcomeTo(game, outcome, observer))
      }
    }

    lastState
  }

  def gameStream[G, S, O, M](
    game: G,
    start: S,
    intro: Boolean = true)(
      implicit evGame: Game[G, S, O, M]): Stream[S] = {
    val end = play(game, start, intro)
    cons(end, gameStream(game, evGame.startFrom(game, end).get, false))
  }

  def playContinuously[G, S, O, M](
    game: G,
    start: S)(
      implicit evGame: Game[G, S, O, M]): S =
    gameStream(game, start).last

}