package org.pingel.bayes

import org.pingel.util.UndirectedGraphEdge

class VariableLink(v1: RandomVariable, v2: RandomVariable) extends UndirectedGraphEdge[RandomVariable](v1, v2) { 

}

