package axle.stats.docalculus

import axle._
import axle.stats._
import axle.stats.docalculus.CausalModel._
import collection._

object Search {

  // val model = MidtermModel1
  // val namer = new VariableNamer()
  //
  // val quantity = model.getQuantity(namer) // Probability
  // val search = new Search()
  // search.reduce(model, quantity, namer, 0, 2)

  /**
   * results.addAll(new AdjustForDirectCauses().apply(quantity, model, namer.duplicate()))
   * TODO try chain rule
   */

  def expand(model: CausalModel, quantity: CausalityProbability, namer: VariableNamer) =
    Some(DeleteObservation(quantity, model, namer.duplicate()) ++
      InsertObservation(quantity, model, namer.duplicate()) ++
      ActionToObservation(quantity, model, namer.duplicate()) ++
      ObservationToAction(quantity, model, namer.duplicate()) ++
      DeleteAction(quantity, model, namer.duplicate()) ++
      InsertAction(quantity, model, namer.duplicate()))

  // TODO: figure out what the intent of the "probFactory" was here:
  def reduce(model: CausalModel, quantity: CausalityProbability, namer: VariableNamer, depth: Int, maxDepth: Int): Option[List[Form]] =
    if (depth <= maxDepth) {
      expand(model, quantity, namer).flatMap(es => {
        es.flatMap(e => {
          val probFactory = CausalityProbability(immutable.Set(), immutable.Set(), immutable.Set())
          if (probFactory.actions.size == 0) {
            Some(List(e))
          } else {
            reduce(model, ??? /* e */ , namer, depth + 1, maxDepth).map(_ ++ List(e))
          }
        }).headOption
      })
    } else {
      Some(Nil)
    }

}
