package axle.pgm.docalculus

import axle.stats.Distribution
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

object Search {

  // val model = MidtermModel1
  // val namer = VariableNamer()
  //
  // val quantity = model.getQuantity(namer) // Probability
  // val search = Search()
  // search.reduce(model, quantity, namer, 0, 2)

  /**
   * results.addAll(new AdjustForDirectCauses().apply(quantity, model, namer.duplicate()))
   * TODO try chain rule
   */

  def expand[T: Eq, N: Field, DG[_, _]](model: CausalModel[T, N, DG], quantity: CausalityProbability[T, N], namer: VariableNamer[T, N]): Some[List[Form]] =
    Some(DeleteObservation(quantity, model, namer.duplicate) ++
      InsertObservation(quantity, model, namer.duplicate) ++
      ActionToObservation(quantity, model, namer.duplicate) ++
      ObservationToAction(quantity, model, namer.duplicate) ++
      DeleteAction(quantity, model, namer.duplicate) ++
      InsertAction(quantity, model, namer.duplicate))

  // TODO: figure out what the intent of the "probFactory" was here:
  def reduce[T: Eq, N: Field, DG[_, _]](model: CausalModel[T, N, DG], quantity: CausalityProbability[T, N], namer: VariableNamer[T, N], depth: Int, maxDepth: Int): Option[List[Form]] =
    if (depth <= maxDepth) {
      expand(model, quantity, namer).flatMap(es => {
        es.flatMap(e => {
          val probFactory = CausalityProbability(Set.empty[Distribution[T, N]], Set.empty[Distribution[T, N]], Set.empty[Distribution[T, N]])
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
