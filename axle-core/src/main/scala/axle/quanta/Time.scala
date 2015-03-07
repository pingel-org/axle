package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Time[N]() extends Quantum4[N] {

  type Q = Time[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def second: UnitOfMeasurement4[Q, N] = unit("second", "s", Some("http://en.wikipedia.org/wiki/Second"))
  def s = second
  def millisecond: UnitOfMeasurement4[Q, N] = unit("millisecond", "ms", Some("http://en.wikipedia.org/wiki/Millisecond"))
  def ms = millisecond
  def microsecond: UnitOfMeasurement4[Q, N] = unit("microsecond", "μs", Some("http://en.wikipedia.org/wiki/Microsecond"))
  def μs = microsecond
  def nanosecond: UnitOfMeasurement4[Q, N] = unit("nanosecond", "ns", Some("http://en.wikipedia.org/wiki/Nanosecond"))
  def ns = nanosecond
  def picosecond: UnitOfMeasurement4[Q, N] = unit("picosecond", "ps", Some("http://en.wikipedia.org/wiki/Picosecond"))
  def ps = picosecond
  def femtosecond: UnitOfMeasurement4[Q, N] = unit("femtosecond", "fs", Some("http://en.wikipedia.org/wiki/Femtosecond"))
  def fs = femtosecond
  def attosecond: UnitOfMeasurement4[Q, N] = unit("attosecond", "as", Some("http://en.wikipedia.org/wiki/Attosecond"))
  def as = attosecond
  def zeptosecond: UnitOfMeasurement4[Q, N] = unit("zeptosecond", "zs", Some("http://en.wikipedia.org/wiki/Zeptosecond"))
  def zs = zeptosecond
  def yoctosecond: UnitOfMeasurement4[Q, N] = unit("yoctosecond", "ys", Some("http://en.wikipedia.org/wiki/Yoctosecond"))
  def ys = yoctosecond
  def minute: UnitOfMeasurement4[Q, N] = unit("minute", "m", Some("http://en.wikipedia.org/wiki/Minute"))
  def m = minute
  def hour: UnitOfMeasurement4[Q, N] = unit("hour", "hr", Some("http://en.wikipedia.org/wiki/Hour"))
  def day: UnitOfMeasurement4[Q, N] = unit("day", "d", Some("http://en.wikipedia.org/wiki/Day"))
  def year: UnitOfMeasurement4[Q, N] = unit("year", "yr", Some("http://en.wikipedia.org/wiki/Year"))
  def century: UnitOfMeasurement4[Q, N] = unit("century", "century", Some("http://en.wikipedia.org/wiki/Century"))
  def millenium: UnitOfMeasurement4[Q, N] = unit("millenium", "ky", Some("http://en.wikipedia.org/wiki/Millenium"))
  def ky = millenium
  def megayear: UnitOfMeasurement4[Q, N] = unit("megayear", "my")
  def my = megayear
  def gigayear: UnitOfMeasurement4[Q, N] = unit("gigayear", "gy")
  def gy = gigayear

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

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(second, millisecond, microsecond, nanosecond, picosecond, femtosecond, attosecond,
      zeptosecond, yoctosecond, minute, hour, day, year, century, millenium, megayear, gigayear)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
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
