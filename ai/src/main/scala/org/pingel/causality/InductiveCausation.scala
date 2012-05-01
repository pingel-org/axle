
package org.pingel.causality

import scala.collection._

import org.pingel.bayes.{ Distribution, RandomVariable, PartiallyDirectedGraph }
import org.pingel.forms.Basic.PBooleans

class InductiveCausation(pHat: Distribution) {
  val FALSE = new PBoolean(false)
  val TRUE = new PBoolean(true)

  var varList = mutable.ListBuffer[RandomVariable]()
  varList ++= pHat.getVariables()

  def prepareGraph(): PartiallyDirectedGraph = {

    val G = new PartiallyDirectedGraph(varList)

    val separators = emptyMatrix[Set[RandomVariable]](varList.size, varList.size)

    for (i <- 0 until varList.size) {
      val a = varList(i)
      for (j <- (i + 1) until varList.size) {
        val b = varList(j)

        val S = pHat.separate(a, b)

        // Connect a and b with an undirected edge iff no set S_ab can be found
        if (S == null) {
          println("cannot separate " + a.name + " and " + b.name)
          G.connect(a, b)
        } else {
          println("separating (" + a.name + ", " + b.name + ") with " + S)
        }

        separators.setValueAt(i, j, S)
        separators.setValueAt(j, i, S)
      }
    }

    for (i <- 0 until (varList.size - 1)) {
      val a = varList(i)
      val aNeighbors = G.links(a, null, null, null)

      for (j <- (i + 1) until varList.size) {
        val b = varList(j)
        if (!G.areAdjacent(a, b)) {
          val S = separators.valueAt(i, j)
          if (S != null) {

            println("prepareGraph second loop")

            val bNeighbors = G.links(b, null, null, null)
            val cList = intersection(aNeighbors, bNeighbors)

            for (c <- cList) {
              if (!S.contains(c)) {
                G.connect(a, c)
                G.orient(a, c)
                G.connect(b, c)
                G.orient(b, c)
              }
            }
          }
        }
      }
    }

    G
  }

  def intersection(v1: List[RandomVariable], v2: List[RandomVariable]) = {

    var result = mutable.ListBuffer[RandomVariable]()

    for (o1 <- v1) {
      var found = false
      var it2 = v2.iterator
      while ((!found) && it2.hasNext) {
        val o2 = it2.next()
        if (o1.equals(o2)) {
          result.append(o1)
          found = true
        }
      }
    }
    result
  }

  def applyICR1(G: PartiallyDirectedGraph): Boolean = {
    // R1: Orient b - c into b -> c whenever there is an arrow
    // a -> b such that a and c are nonadjacent

    var applied = false
    for (i <- 0 until (varList.size - 1)) {
      val a = varList(i)
      for (b <- G.links(a, null, null, TRUE)) {
        for (c <- G.links(b, null, null, FALSE)) {
          if (!G.areAdjacent(a, c)) {
            G.orient(b, c)
            applied = true
          }
        }
      }
    }
    applied
  }

  def applyICR2(G: PartiallyDirectedGraph): Boolean = {

    // R2: Orient a - b into a -> b whenever there is chain a -> c -> b

    var applied = false
    for (a <- varList) {
      for (c <- G.links(a, null, null, TRUE)) {
        for (b <- G.links(c, null, null, TRUE)) {
          if (G.undirectedAdjacent(a, b)) {
            G.orient(a, b)
            applied = true
          }
        }
      }
    }
    applied
  }

  def applyICR3(G: PartiallyDirectedGraph): Boolean = {
    // R3: Orient a - b into a -> b whenever there are two chains
    // a - c -> b and a - d -> b such that c and d are nonadjacent

    var applied = false
    for (i <- 0 until varList.size) {
      val a = varList(i)
      val aNeighbors = G.links(a, null, null, FALSE)
      for (j <- 0 until (aNeighbors.size - 1)) {
        val c = aNeighbors(j)
        for (m <- (j + 1) until aNeighbors.size) {
          val d = aNeighbors(m)
          if (!G.undirectedAdjacent(c, d)) {
            val dOutputs = G.links(d, None, None, Some(true))
            val cOutputs = G.links(c, None, None, Some(true))
            val dcOutputs = intersection(dOutputs, cOutputs)
            for (b <- dcOutputs) {
              if (G.undirectedAdjacent(a, b)) {
                G.orient(a, b)
                applied = true
              }
            }
          }
        }
      }
    }
    applied
  }

  def applyICR4(G: PartiallyDirectedGraph): Boolean = {
    // R4: Orient a - b into a -> b whenever there are two chains
    // a - c -> d and c -> d -> b such that c and b are nonadjacent
    // and a and d are adjacent.

    var applied = false
    for (i <- 0 until varList.size) {
      val a = varList(i)
      val aNeighbors = G.links(a, None, None, Some(true))
      for (j <- 0 until aNeighbors.size) {
        val c = aNeighbors(j)
        val cOutputs = G.links(c, None, None, Some(true))
        for (m <- 0 until cOutputs.size) {
          val d = cOutputs(m)
          if (!a.equals(d)) {
            val dOutputs = G.links(d, None, None, Some(true))
            if (G.areAdjacent(a, d)) {
              val adOutputs = intersection(aNeighbors, dOutputs)
              for (n <- 0 until adOutputs.size) {
                val b = adOutputs(n)
                if ((!b.equals(c)) && (!G.areAdjacent(c, b))) {
                  G.orient(a, b)
                  applied = true
                }
              }
            }
          }
        }
      }
    }
    applied
  }

  def applyICStarR1(G: PartiallyDirectedGraph): Boolean = {
    /*
    	 * R1: For each pair of nonadjacent nodes a and b with a common neighbor c,
    	 * if the link between a and c has an arrowhead into c and if the link between
    	 * c and b has no arrowhead into c, then add an arrowhead on the link between
	 	 * c and b pointing at b and mark that link to obtain c -*-> b
	 	 */

    var applied = false

    for (i <- 0 until varList.size) {

      val a = varList(i)

      val cList = G.links(a, None, Some(false), Some(true))

      for (j <- 0 until cList.size) {
        val c = cList(j)
        val bList = G.links(c, Some(false), Some(false), None)
        for (m <- 0 until bList.size) {
          val b = bList(m)
          if (!G.areAdjacent(a, b)) {
            G.orient(c, b)
            G.mark(c, b)
            applied = true
          }
        }
      }
    }
    applied
  }

  def applyICStarR2(G: PartiallyDirectedGraph): Boolean = {

    /* R2: If a and b are adjacent and there is a directed path (composed strictly
   		 * of marked links) from a to b (as in Figure 2.2), then add an arrowhead
   		 * pointing toward b on the link between a and b.
    	*/

    var applied = false
    for (a <- varList) {
      for (b <- G.links(a, null, null, null)) {
        if (G.markedPathExists(a, b)) {
          G.orient(a, b);
        }
      }
    }
    applied
  }

  def ic(): PartiallyDirectedGraph = {
    // This code is based on the pseudocode in Pearl's "Causation" page 50

    // TODO assert: "pHat" is stable

    var G = prepareGraph()

    var proceed = true
    while (proceed) {
      println("proceeding")
      proceed = applyICR1(G)
      proceed |= applyICR2(G)
      proceed |= applyICR3(G)
      proceed |= applyICR4(G)
    }
    G
  }

  def icstar(): PartiallyDirectedGraph = {
    // This is from page 52 - 53

    var G = prepareGraph()

    /* In the partially directed graph that results, add (recursively) as many
    	 * arrowheads as possible, and mark as many edges as possible,
    	 * according to the following two rules:
    	 */

    var proceed = true
    while (proceed) {
      proceed = applyICStarR1(G)
      proceed |= applyICStarR2(G)
    }
    G
  }

}
