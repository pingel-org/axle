package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

import spire.math.Rational

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

case class Time() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"

}

abstract class TimeMetadata[N: Field: Eq, DG[_, _]: DirectedGraph]
  extends QuantumMetadataGraph[Time, N, DG] {

  type U = UnitOfMeasurement[Time, N]

  def second: U
  def s: U
  def millisecond: U
  def ms: U
  def microsecond: U
  def μs: U
  def nanosecond: U
  def ns: U
  def picosecond: U
  def ps: U
  def femtosecond: U
  def fs: U
  def attosecond: U
  def as: U
  def zeptosecond: U
  def zs: U
  def yoctosecond: U
  def ys: U
  def minute: U
  def m: U
  def hour: U
  def day: U
  def year: U
  def century: U
  def millenium: U
  def ky: U
  def megayear: U
  def my: U
  def gigayear: U
  def gy: U

}

object Time {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] = new TimeMetadata[N, DG] {

    def unit(name: String, symbol: String, wiki: Option[String] = None) =
      UnitOfMeasurement[Time, N](name, symbol, wiki)

    lazy val _second = unit("second", "s", Some("http://en.wikipedia.org/wiki/Second"))
    lazy val _millisecond = unit("millisecond", "ms", Some("http://en.wikipedia.org/wiki/Millisecond"))
    lazy val _microsecond = unit("microsecond", "μs", Some("http://en.wikipedia.org/wiki/Microsecond"))
    lazy val _nanosecond = unit("nanosecond", "ns", Some("http://en.wikipedia.org/wiki/Nanosecond"))
    lazy val _picosecond = unit("picosecond", "ps", Some("http://en.wikipedia.org/wiki/Picosecond"))
    lazy val _femtosecond = unit("femtosecond", "fs", Some("http://en.wikipedia.org/wiki/Femtosecond"))
    lazy val _attosecond = unit("attosecond", "as", Some("http://en.wikipedia.org/wiki/Attosecond"))
    lazy val _zeptosecond = unit("zeptosecond", "zs", Some("http://en.wikipedia.org/wiki/Zeptosecond"))
    lazy val _yoctosecond = unit("yoctosecond", "ys", Some("http://en.wikipedia.org/wiki/Yoctosecond"))
    lazy val _minute = unit("minute", "m", Some("http://en.wikipedia.org/wiki/Minute"))
    lazy val _hour = unit("hour", "hr", Some("http://en.wikipedia.org/wiki/Hour"))
    lazy val _day = unit("day", "d", Some("http://en.wikipedia.org/wiki/Day"))
    lazy val _year = unit("year", "yr", Some("http://en.wikipedia.org/wiki/Year"))
    lazy val _century = unit("century", "century", Some("http://en.wikipedia.org/wiki/Century"))
    lazy val _millenium = unit("millenium", "ky", Some("http://en.wikipedia.org/wiki/Millenium"))
    lazy val _megayear = unit("megayear", "my")
    lazy val _gigayear = unit("gigayear", "gy")

    def second = _second
    def s = _second
    def millisecond = _millisecond
    def ms = _millisecond
    def microsecond = _microsecond
    def μs = _microsecond
    def nanosecond = _nanosecond
    def ns = _nanosecond
    def picosecond = _picosecond
    def ps = _picosecond
    def femtosecond = _femtosecond
    def fs = _femtosecond
    def attosecond = _attosecond
    def as = _attosecond
    def zeptosecond = _zeptosecond
    def zs = _zeptosecond
    def yoctosecond = _yoctosecond
    def ys = _yoctosecond
    def minute = _minute
    def m = _minute
    def hour = _hour
    def day = _day
    def year = _year
    def century = _century
    def millenium = _millenium
    def ky = _millenium
    def megayear = _megayear
    def my = _megayear
    def gigayear = _gigayear
    def gy = _gigayear

    def units: List[UnitOfMeasurement[Time, N]] =
      List(second, millisecond, microsecond, nanosecond, picosecond, femtosecond, attosecond,
        zeptosecond, yoctosecond, minute, hour, day, year, century, millenium, megayear, gigayear)

    def links: Seq[(UnitOfMeasurement[Time, N], UnitOfMeasurement[Time, N], Bijection[N, N])] =
      List[(UnitOfMeasurement[Time, N], UnitOfMeasurement[Time, N], Bijection[N, N])](
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

}