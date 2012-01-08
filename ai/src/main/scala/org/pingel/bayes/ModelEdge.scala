package org.pingel.bayes;

import org.pingel.util.DirectedGraphEdge

class ModelEdge(v1: RandomVariable, v2: RandomVariable)
extends DirectedGraphEdge[RandomVariable]
{
  def getSource = v1
  def getDest = v2
}
