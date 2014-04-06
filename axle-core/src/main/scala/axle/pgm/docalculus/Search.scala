package axle.pgm.docalculus

import axle._
import axle.stats._
import axle.pgm._
import CausalModel._
import spire.algebra._
import spire.implicits._

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

  def expand[T: Eq, N: Field](model: CausalModel[T, N], quantity: CausalityProbability[T, N], namer: VariableNamer[T, N]): Some[List[Form]] =
    Some(DeleteObservation(quantity, model, namer.duplicate) ++
      InsertObservation(quantity, model, namer.duplicate) ++
      ActionToObservation(quantity, model, namer.duplicate) ++
      ObservationToAction(quantity, model, namer.duplicate) ++
      DeleteAction(quantity, model, namer.duplicate) ++
      InsertAction(quantity, model, namer.duplicate))

  // TODO: figure out what the intent of the "probFactory" was here:
  def reduce[T: Eq, N: Field](model: CausalModel[T, N], quantity: CausalityProbability[T, N], namer: VariableNamer[T, N], depth: Int, maxDepth: Int): Option[List[Form]] =
    if (depth <= maxDepth) {
      expand(model, quantity, namer).flatMap(es => {
        es.flatMap(e => {
          val probFactory = CausalityProbability(Set.empty[RandomVariable[T, N]], Set.empty[RandomVariable[T, N]], Set.empty[RandomVariable[T, N]])
          if (probFactory.actions.size === 0) {
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
