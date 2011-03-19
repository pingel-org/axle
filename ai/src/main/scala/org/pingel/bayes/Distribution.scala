package org.pingel.bayes

import java.util.ArrayList
import java.util.List

abstract class Distribution(variables: List[RandomVariable]) {

  def getVariables(): List[RandomVariable] = variables

}
