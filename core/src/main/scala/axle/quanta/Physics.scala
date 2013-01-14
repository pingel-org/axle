package axle.quanta

import axle.graph._

/**
 * The "Physics" objects models the graph of units
 *
 * See http://en.wikipedia.org/wiki/SI_derived_unit
 *
 */

object Physics {

  val qs = Vector(
    Mass, Distance, Information, Time,
    Angle,
    Area, Volume, Flow, Speed, Acceleration,
    Force, Energy, Power
  )

  val derivations = Vector(
    Area is Distance * Distance,
    Volume is Distance * Distance * Distance,
    Speed is Distance / Time,
    Acceleration is Speed / Time,
    Force is Mass * Acceleration,
    Energy is Power * Time,
    Power is Energy / Time,
    Flow is Volume / Time
  )

//  {
//    import Distance._
//    import Area._
//    
//    val x: Area.Q = (1 *: meter) by (1 *: meter)
//  }

}