package org.pingel.axle.quanta

object Volume extends Quantum {

  import Quantity._

  import Distance._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"
    
  val derivations = List(Distance cubed)

  val m3 = meter cubed
  
  val km3 = kilometer cubed
  
  val greatLakes = Quantity("22671", km3, Some("Great Lakes Volume"), None, Some("http://en.wikipedia.org/wiki/Great_Lakes"))
  
}