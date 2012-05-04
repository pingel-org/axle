
package org.pingel.causality

import scala.collection._
import org.pingel.bayes.Model
import org.pingel.forms.Basic.PFunction

import axle.util.Collector
import axle.iterator.CrossProduct
import axle.iterator.PowerSet
import org.pingel.bayes.{ RandomVariable, Factor, Case, Probability }

class CausalModel(name: String) extends Model(name) {

  var variable2function = Map[RandomVariable, PFunction]()

  def sampleDistribution(numSamples: Int) = {
    println("creating probabilitytable of " + getObservableRandomVariables().size() + " variables")
    val result = new Factor(getObservableRandomVariables())
    for (j <- 0 until numSamples) {
      val sample = getSample()
      val previous = result.read(sample)
      result.write(sample, previous + 1)
    }
    result
  }

  def addFunction(f: PFunction) = {
    variable2function += f.rv -> f
    for (i <- f.inputs) {
      connect(i, f.rv)
    }
  }

  def getFunction(v: RandomVariable) = variable2function.get(v)

  def getSample(): Case = {
    val all = new Case()
    for (rv <- getRandomVariables()) {
      getFunction(rv).execute(this, all)
    }
    all.projectToVars(getObservableRandomVariables())
  }

  def isSemiMarkovian() = getGraph().isAcyclic()

  // page 69.  I'm not confident I have this right.
  def isMarkovian(): Boolean =
    getRandomVariables().forall(rv => (rv.observable || (g.getSuccessors(rv).size <= 1)))

  def identifies(p: Probability): Boolean = {
    if (isMarkovian) {
      // page 78 Theorem 3.2.5
      p.getActions().forall(rv => rv.observable && g.getPredecessors(rv).forall(_.observable)) &&
        p.getQuestion().forall(_.observable)
    } else if (isSemiMarkovian) {
      hasDoor(p)
    } else {
      // TODO chapter 7 can help??
      throw new UnsupportedOperationException()
    }
  }

  def rvGetter = new Collector[Variable, RandomVariable]() {
    override def function(variable: Variable) = variable.getRandomVariable()
  }

  def hasDoor(p: Probability): Boolean = {
    val V = mutable.Set[RandomVariable]()
    val questionRVs = rvGetter.execute(p.getQuestion())
    val actionRVs = rvGetter.execute(p.getActions())
    for (rv <- getRandomVariables()) {
      if (rv.observable && !questionRVs.contains(rv) && !actionRVs.contains(rv)) {
        V += rv
      }
    }

    // a very naive search strategy

    for (pair <- new CrossProduct[RandomVariable](actionRVs, questionRVs)) {
      for (Z <- new PowerSet[RandomVariable](V)) {
        if (satisfiesBackdoorCriterion(pair.get(0), pair.get(1), Z) ||
          satisfiesFrontdoorCriterion(pair.get(0), pair.get(1), Z)) {
          return true
        }
      }
    }
    false
  }

  def allBackdoorsBlocked(XiSet: Set[RandomVariable], XjSet: Set[RandomVariable], Z: Set[RandomVariable]) = {
    val subModel = duplicate()
    subModel.getGraph().removeOutputs(XiSet)
    subModel.blocks(XiSet, XjSet, Z)
  }

  def satisfiesBackdoorCriterion(Xi: RandomVariable, Xj: RandomVariable, Z: Set[RandomVariable]): Boolean = {
    // Definition 3.3.1; page 79

    // i) no node in Z is a descendant of Xi

    var descendants = Set[RandomVariable]()
    g.collectDescendants(Xi, descendants)
    for (z <- Z) {
      if (descendants.contains(z)) {
        return false
      }
    }

    // and

    // ii) Z blocks every path between Xi and Xj that contains an arrow into Xi

    val XiSet = Set[RandomVariable](Xi)
    val XjSet = Set[RandomVariable](Xj)
    allBackdoorsBlocked(XiSet, XjSet, Z)

  }

  def pathsInterceptBefore(from: RandomVariable, interceptors: Set[RandomVariable], to: RandomVariable): Boolean = {
    if (from == to) {
      return false
    }

    if (!interceptors.contains(from)) {
      for (rv <- getGraph().getSuccessors(from)) {
        if (!pathsInterceptBefore(rv, interceptors, to)) {
          return false
        }
      }
    }

    true
  }

  def satisfiesFrontdoorCriterion(X: RandomVariable, Y: RandomVariable, Z: Set[RandomVariable]) = {
    // Definition 3.3.3 page 82

    // i) Z intercepts all directed paths from X to Y;
    // ii) there is no back-door path from X to Z; and
    // iii) all back-door paths from Z to Y are blocked by X

    val XSet = Set[RandomVariable](X)
    val YSet = Set[RandomVariable](Y)

    pathsInterceptBefore(X, Z, Y) &&
      allBackdoorsBlocked(XSet, Z, Set[RandomVariable]()) &&
      allBackdoorsBlocked(Z, YSet, XSet)
  }

  def duplicate() = {
    val answer = new CausalModel(name)
    for (v <- getRandomVariables()) {
      answer.addVariable(v)
    }
    for (v <- getRandomVariables()) {
      for (output <- getGraph().getSuccessors(v)) {
        answer.connect(v, output)
      }
    }
    answer.variable2function = mutable.Map[RandomVariable, PFunction]() ++ variable2function
    answer
  }

}
