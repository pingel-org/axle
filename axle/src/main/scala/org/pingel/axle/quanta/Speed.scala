package org.pingel.axle.quanta

object Speed extends Quantum {

  import Quantity._
  
  import Distance._
  import Time._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"
  val derivations = List(Distance / Time)

  val mps = meter / second
  val mph = mile / hour
  val c = Quantity("299792458", mps, Some("Light Speed"), Some("c"), Some("http://en.wikipedia.org/wiki/Speed_of_light"))
  val speedLimit = Quantity("65", mph, Some("Speed limit"), None)

}