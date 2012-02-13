package org.pingel.axle.quanta

object Speed extends Quantum {

  import Distance._
  import Time._
  
  val wikipediaUrl = "http://en.wikipedia.org/wiki/Speed"

  val mps = meter / second
  val mph = mile / hour

  val unitsOfMeasurement = List(mps, mph)

  val derivations = List(Distance / Time)

  val c = Quantity(299792458.0, mps, Some("Light Speed"), Some("http://en.wikipedia.org/wiki/Speed_of_light"))
  
  val examples = List(
      Quantity(65.0, mph, Some("Speed limit"), None),
      c
  )

}