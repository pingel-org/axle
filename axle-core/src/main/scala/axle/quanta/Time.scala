package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Time() extends Quantum("http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)")

object Time {

  type Q = Time

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def second[N]: UnitOfMeasurement[Q, N] = unit("second", "s", Some("http://en.wikipedia.org/wiki/Second"))
  def s[N] = second[N]
  def millisecond[N]: UnitOfMeasurement[Q, N] = unit("millisecond", "ms", Some("http://en.wikipedia.org/wiki/Millisecond"))
  def ms[N] = millisecond[N]
  def microsecond[N]: UnitOfMeasurement[Q, N] = unit("microsecond", "μs", Some("http://en.wikipedia.org/wiki/Microsecond"))
  def μs[N] = microsecond[N]
  def nanosecond[N]: UnitOfMeasurement[Q, N] = unit("nanosecond", "ns", Some("http://en.wikipedia.org/wiki/Nanosecond"))
  def ns[N] = nanosecond[N]
  def picosecond[N]: UnitOfMeasurement[Q, N] = unit("picosecond", "ps", Some("http://en.wikipedia.org/wiki/Picosecond"))
  def ps[N] = picosecond[N]
  def femtosecond[N]: UnitOfMeasurement[Q, N] = unit("femtosecond", "fs", Some("http://en.wikipedia.org/wiki/Femtosecond"))
  def fs[N] = femtosecond[N]
  def attosecond[N]: UnitOfMeasurement[Q, N] = unit("attosecond", "as", Some("http://en.wikipedia.org/wiki/Attosecond"))
  def as[N] = attosecond[N]
  def zeptosecond[N]: UnitOfMeasurement[Q, N] = unit("zeptosecond", "zs", Some("http://en.wikipedia.org/wiki/Zeptosecond"))
  def zs[N] = zeptosecond[N]
  def yoctosecond[N]: UnitOfMeasurement[Q, N] = unit("yoctosecond", "ys", Some("http://en.wikipedia.org/wiki/Yoctosecond"))
  def ys[N] = yoctosecond[N]
  def minute[N]: UnitOfMeasurement[Q, N] = unit("minute", "m", Some("http://en.wikipedia.org/wiki/Minute"))
  def m[N] = minute[N]
  def hour[N]: UnitOfMeasurement[Q, N] = unit("hour", "hr", Some("http://en.wikipedia.org/wiki/Hour"))
  def day[N]: UnitOfMeasurement[Q, N] = unit("day", "d", Some("http://en.wikipedia.org/wiki/Day"))
  def year[N]: UnitOfMeasurement[Q, N] = unit("year", "yr", Some("http://en.wikipedia.org/wiki/Year"))
  def century[N]: UnitOfMeasurement[Q, N] = unit("century", "century", Some("http://en.wikipedia.org/wiki/Century"))
  def millenium[N]: UnitOfMeasurement[Q, N] = unit("millenium", "ky", Some("http://en.wikipedia.org/wiki/Millenium"))
  def ky[N] = millenium[N]
  def megayear[N]: UnitOfMeasurement[Q, N] = unit("megayear", "my")
  def my[N] = megayear[N]
  def gigayear[N]: UnitOfMeasurement[Q, N] = unit("gigayear", "gy")
  def gy[N] = gigayear[N]

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

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(second, millisecond, microsecond, nanosecond, picosecond, femtosecond, attosecond,
      zeptosecond, yoctosecond, minute, hour, day, year, century, millenium, megayear, gigayear)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
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

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum.cgn(units[N], links)

}
