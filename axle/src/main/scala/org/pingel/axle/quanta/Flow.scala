package org.pingel.axle.quanta

object Flow extends Quantum {

  import Quantity._

  import Volume._
  import Time._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volumetric_flow_rate"
    
  val derivations = List(Volume / Time)

  val m3s = m3 / second

}