/*
 * Created on Jun 2, 2005
 *
 */
package org.pingel.bayes

class VariableNamer {

  var counts = Map[RandomVariable, Integer]()
    
  def increment(rv: RandomVariable): Integer = {
    var c = 0
    var count: Integer = counts(rv)
    if( counts.contains(rv) ) {
      c = counts(rv)
    }
    counts += rv -> new Integer(c+1)
    c
  }

  def duplicate(): VariableNamer = {
    var duplicate: VariableNamer = new VariableNamer()
    duplicate.counts.putAll(counts)
    duplicate
  }
}
