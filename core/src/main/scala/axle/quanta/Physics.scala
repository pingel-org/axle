package axle.quanta

/**
 * The "Physics" objects models the graph of units
 *
 * See http://en.wikipedia.org/wiki/SI_derived_unit
 *
 */

object Physics {
  
  val basic = Vector(
    Mass, Distance, Information, Time
  )

  Area is Distance * Distance

  Volume is (Distance * Distance) * Distance

  Speed is Distance / Time

  Acceleration is Speed / Time

  Force is Mass * Acceleration

  Energy is Power * Time

  Power is Energy / Time

  Flow is Volume / Time

}