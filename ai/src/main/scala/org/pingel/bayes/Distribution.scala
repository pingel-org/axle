package org.pingel.bayes

abstract class Distribution(variables: List[RandomVariable]) {

  def getVariables(): List[RandomVariable] = variables

}
