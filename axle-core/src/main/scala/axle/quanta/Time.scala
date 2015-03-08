package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

import spire.math.Rational

object TimeRational extends Time[Rational]()

case class Time[N]() extends Quantum[N] {

  type Q = Time[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Time[N], N](name, symbol, wiki)

  lazy val second = unit("second", "s", Some("http://en.wikipedia.org/wiki/Second"))
  lazy val s = second
  lazy val millisecond = unit("millisecond", "ms", Some("http://en.wikipedia.org/wiki/Millisecond"))
  lazy val ms = millisecond
  lazy val microsecond = unit("microsecond", "μs", Some("http://en.wikipedia.org/wiki/Microsecond"))
  lazy val μs = microsecond
  lazy val nanosecond = unit("nanosecond", "ns", Some("http://en.wikipedia.org/wiki/Nanosecond"))
  lazy val ns = nanosecond
  lazy val picosecond = unit("picosecond", "ps", Some("http://en.wikipedia.org/wiki/Picosecond"))
  lazy val ps = picosecond
  lazy val femtosecond = unit("femtosecond", "fs", Some("http://en.wikipedia.org/wiki/Femtosecond"))
  lazy val fs = femtosecond
  lazy val attosecond = unit("attosecond", "as", Some("http://en.wikipedia.org/wiki/Attosecond"))
  lazy val as = attosecond
  lazy val zeptosecond = unit("zeptosecond", "zs", Some("http://en.wikipedia.org/wiki/Zeptosecond"))
  lazy val zs = zeptosecond
  lazy val yoctosecond = unit("yoctosecond", "ys", Some("http://en.wikipedia.org/wiki/Yoctosecond"))
  lazy val ys = yoctosecond
  lazy val minute = unit("minute", "m", Some("http://en.wikipedia.org/wiki/Minute"))
  lazy val m = minute
  lazy val hour = unit("hour", "hr", Some("http://en.wikipedia.org/wiki/Hour"))
  lazy val day = unit("day", "d", Some("http://en.wikipedia.org/wiki/Day"))
  lazy val year = unit("year", "yr", Some("http://en.wikipedia.org/wiki/Year"))
  lazy val century = unit("century", "century", Some("http://en.wikipedia.org/wiki/Century"))
  lazy val millenium = unit("millenium", "ky", Some("http://en.wikipedia.org/wiki/Millenium"))
  lazy val ky = millenium
  lazy val megayear = unit("megayear", "my")
  lazy val my = megayear
  lazy val gigayear = unit("gigayear", "gy")
  lazy val gy = gigayear

  //  def units[N] = List[UnitOfMeasurement[Q, N]](
  //    unit("second", "s", Some("http://en.wikipedia.org/wiki/Second")),
  //    unit("millisecond", "ms", Some("http://en.wikipedia.org/wiki/Millisecond")),
  //    unit("microsecond", "μs", Some("http://en.wikipedia.org/wiki/Microsecond")),
  //    unit("nanosecond", "ns", Some("http://en.wikipedia.org/wiki/Nanosecond")),
  //    unit("picosecond", "ps", Some("http://en.wikipedia.org/wiki/Picosecond")),
  //    unit("femtosecond", "fs", Some("http://en.wikipedia.org/wiki/Femtosecond")),
  //    unit("attosecond", "as", Some("http://en.wikipedia.org/wiki/Attosecond")),
  //    unit("zeptosecond", "zs", Some("http://en.wikipedia.org/wiki/Zeptosecond")),
  //    unit("yoctosecond", "ys", Some("http://en.wikipedia.org/wiki/Yoctosecond")),
  //    unit("minute", "m", Some("http://en.wikipedia.org/wiki/Minute")),
  //    unit("hour", "hr", Some("http://en.wikipedia.org/wiki/Hour")),
  //    unit("day", "d", Some("http://en.wikipedia.org/wiki/Day")),
  //    unit("year", "yr", Some("http://en.wikipedia.org/wiki/Year")),
  //    unit("century", "century", Some("http://en.wikipedia.org/wiki/Century")),
  //    unit("millenium", "ky", Some("http://en.wikipedia.org/wiki/Millenium")),
  //    unit("megayear", "my"),
  //    unit("gigayear", "gy"))

  //  //  lazy val globalLifeExpectancy = 67.2 *: year // Some("2010 global average life expectancy"), None, Some("http://en.wikipedia.org/wiki/Life_expectancy"))

  //  //  // Distant Past:
  //  //  lazy val universeAge = 13.7 *: gy // Some("universe age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Universe"))
  //  //  lazy val earthAge = 4.54 *: gy // Some("earth age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Earth"))
  //  //  lazy val simpleCellsAge = 3.8 *: gy // Some("simple cells evolve"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  //  lazy val multiCellularLifeAge = 1 *: gy // Some("multi-cellular life evolves"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  //  lazy val fungiAge = 560 *: my // Some("kingdom Fungi age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  //  lazy val classMammalAge = 215 *: my // Some("class Mammalia age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  //  lazy val primateAge = 60 *: my // Some("order Primate age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  //  lazy val australopithecusAge = 4 *: my // Some("genus Australopithecus age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  //  lazy val modernHumanAge = 200 *: ky // Some("anatomically modern human age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))

  def units: List[UnitOfMeasurement[Time[N], N]] =
    List(second, millisecond, microsecond, nanosecond, picosecond, femtosecond, attosecond,
      zeptosecond, yoctosecond, minute, hour, day, year, century, millenium, megayear, gigayear)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement[Time[N], N], UnitOfMeasurement[Time[N], N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Time[N], N], UnitOfMeasurement[Time[N], N], Bijection[N, N])](
      (ms, s, Scale10s(3)),
      (μs, s, Scale10s(6)),
      (ns, s, Scale10s(9)),
      (ps, s, Scale10s(12)),
      (fs, s, Scale10s(15)),
      (as, s, Scale10s(18)),
      (zs, s, Scale10s(21)),
      (ys, s, Scale10s(24)),
      (s, m, ScaleInt(60)),
      (m, hour, ScaleInt(60)),
      (hour, day, ScaleInt(24)),
      (day, year, ScaleDouble(365.25)),
      (year, century, Scale10s(2)),
      (year, ky, Scale10s(3)),
      (year, my, Scale10s(6)),
      (year, gy, Scale10s(9)))

}
