package axle.quanta

import axle.algebra._

/**
 * The "Physics" objects models the graph of units
 *
 * See http://en.wikipedia.org/wiki/SI_derived_unit
 *
 */

object Physics {

  val qs = Vector(
    Acceleration,
    Angle,
    Area,
    Distance,
    Energy,
    Flow,
    Force,
    Frequency,
    Information,
    Mass,
    Money,
    MoneyFlow,
    MoneyPerForce,
    Power,
    Speed,
    Time,
    Volume)

  // TODO derived units
  //      derive(mps.over[Time.type, this.type](second, this)),
  //      derive(fps.over[Time.type, this.type](second, this)),
  //      derive(meter.by[Distance.type, this.type](meter, this), Some("m2"), Some("m2")),
  //      derive(km.by[Distance.type, this.type](km, this), Some("km2"), Some("km2")),
  //      derive(cm.by[Distance.type, this.type](cm, this), Some("cm2"), Some("cm2"))),
  //      derive(kilowatt.by[Time.type, this.type](hour, this)),
  //      derive(m3.over[Time.type, this.type](second, this), Some("cubic meters per second"), Some("m^3/s")),
  //      derive(USD.by[Time.type, this.type](hour, this), Some("$/hr"), Some("$/hr"))),
  //      derive(USD.by[Force.type, this.type](pound, this), Some("$/lb"), Some("$/lb"))),
  //      derive(meter.over[Time.type, this.type](second, this), Some("mps")),
  //      derive(ft.over[Time.type, this.type](second, this), Some("fps")),
  //      derive(mile.over[Time.type, this.type](hour, this), Some("mph")),
  //      derive(km.over[Time.type, this.type](hour, this), Some("kph")),
  //      derive(m2.by[Distance.type, this.type](meter, this), Some("m3"), Some("m3")),
  //      derive(km2.by[Distance.type, this.type](km, this), Some("km3"), Some("km3")),
  //      derive(cm2.by[Distance.type, this.type](cm, this), Some("cm3"), Some("cm3")),

  //  val derivations = Vector(
  //    Area is Distance * Distance,
  //    Volume is Distance * Distance * Distance,
  //    Speed is Distance / Time,
  //    Acceleration is Speed / Time,
  //    Force is Mass * Acceleration,
  //    Energy is Power * Time,
  //    Power is Energy / Time,
  //    Flow is Volume / Time
  //  )

  //  {
  //    import Distance._
  //    import Area._
  //
  //    val x: Area.Q = (1 *: meter) by (1 *: meter)
  //  }

}
