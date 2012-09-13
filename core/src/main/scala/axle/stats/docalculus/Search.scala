

package axle.stats.docalculus

import axle.stats._
import collection._

object Search {

  //  def main(args: Array[String]) {
  //    val model = MidtermModel1
  //    val namer = new VariableNamer()
  //
  //    val quantity = model.getQuantity(namer) // Probability
  //    val search = new Search()
  //    search.reduce(model, quantity, namer, 0, 2)
  //  }

  /**
   * results.addAll(new AdjustForDirectCauses().apply(quantity, model, namer.duplicate()))
   * TODO try chain rule
   */

  def expand(model: Model[RandomVariable[_]], quantity: CausalityProbability, namer: VariableNamer) =
    DeleteObservation(quantity, model, namer.duplicate()) ++
      InsertObservation(quantity, model, namer.duplicate()) ++
      ActionToObservation(quantity, model, namer.duplicate()) ++
      ObservationToAction(quantity, model, namer.duplicate()) ++
      DeleteAction(quantity, model, namer.duplicate()) ++
      InsertAction(quantity, model, namer.duplicate())

  def reduce(model: Model[RandomVariable[_]], quantity: CausalityProbability, namer: VariableNamer, depth: Int, maxDepth: Int): List[Form] = {
    if (depth <= maxDepth) {
      val next = expand(model, quantity, namer)
      if (next != null) {

        for (e <- next) {
          for (i <- 0 until depth) {
            print("\t")
          }
          val probFactory = CausalityProbability(Set(), Set(), Set())
          if (probFactory.isCreatorOf(e)) {
            if (probFactory.getActionSize(e) == 0) {
              return List(e)
            } else {
              val pathThroughQ = reduce(model, e, namer, depth + 1, maxDepth)
              if (pathThroughQ != null) {
                pathThroughQ.add(e)
                return pathThroughQ
              }
            }
          } else {
            println("THIS CASE IS NOT HANDLED")
            return null
          }
        }
      }
    }
    return null
  }

}
