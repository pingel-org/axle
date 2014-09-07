package axle.quanta

import axle.graph.DirectedGraph
import axle.graph.Vertex
import axle.algebra.Bijection
import spire.algebra.Eq
import spire.algebra.Field
import spire.implicits.DoubleAlgebra
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits._
import spire.math.Rational
import spire.math.Real

abstract class Time extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)"
}

object Time extends Time {

  import spire.implicits._

  type Q = Time

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("second", "s", Some("http://en.wikipedia.org/wiki/Second")),
    unit("millisecond", "ms", Some("http://en.wikipedia.org/wiki/Millisecond")),
    unit("microsecond", "μs", Some("http://en.wikipedia.org/wiki/Microsecond")),
    unit("nanosecond", "ns", Some("http://en.wikipedia.org/wiki/Nanosecond")),
    unit("picosecond", "ps", Some("http://en.wikipedia.org/wiki/Picosecond")),
    unit("femtosecond", "fs", Some("http://en.wikipedia.org/wiki/Femtosecond")),
    unit("attosecond", "as", Some("http://en.wikipedia.org/wiki/Attosecond")),
    unit("zeptosecond", "zs", Some("http://en.wikipedia.org/wiki/Zeptosecond")),
    unit("yoctosecond", "ys", Some("http://en.wikipedia.org/wiki/Yoctosecond")),
    unit("minute", "m", Some("http://en.wikipedia.org/wiki/Minute")),
    unit("hour", "hr", Some("http://en.wikipedia.org/wiki/Hour")),
    unit("day", "d", Some("http://en.wikipedia.org/wiki/Day")),
    unit("year", "yr", Some("http://en.wikipedia.org/wiki/Year")),
    unit("century", "century", Some("http://en.wikipedia.org/wiki/Century")),
    unit("millenium", "ky", Some("http://en.wikipedia.org/wiki/Millenium")),
    unit("megayear", "my"),
    unit("gigayear", "gy"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N]
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
  }

  def yoctosecond[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "yoctosecond")
  def ys[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "yoctosecond")
  def zeptosecond[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "zeptosecond")
  def zs[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "zeptosecond")
  def attosecond[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "attosecond")
  def as[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "attosecond")
  def femtosecond[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "femtosecond")
  def fs[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "femtosecond")
  def picosecond[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "picosecond")
  def ps[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "picosecond")
  def nanosecond[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "nanosecond")
  def ns[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "nanosecond")
  def microsecond[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "microsecond")
  def μs[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "microsecond")
  def millisecond[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "millisecond")
  def ms[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "millisecond")
  def second[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "second")
  def s[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "second")
  def minute[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "minute")
  def m[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "minute")
  def hour[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "hour")
  def day[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "day")
  def year[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "year")
  def century[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "century")
  def millenium[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "millenium")
  def ky[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "millenium")
  def my[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "megayear")
  def gy[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "gigayear")

  //  lazy val globalLifeExpectancy = 67.2 *: year // Some("2010 global average life expectancy"), None, Some("http://en.wikipedia.org/wiki/Life_expectancy"))
  //
  //  // Distant Past:
  //  lazy val universeAge = 13.7 *: gy // Some("universe age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Universe"))
  //  lazy val earthAge = 4.54 *: gy // Some("earth age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Earth"))
  //  lazy val simpleCellsAge = 3.8 *: gy // Some("simple cells evolve"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  lazy val multiCellularLifeAge = 1 *: gy // Some("multi-cellular life evolves"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  lazy val fungiAge = 560 *: my // Some("kingdom Fungi age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  lazy val classMammalAge = 215 *: my // Some("class Mammalia age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  lazy val primateAge = 60 *: my // Some("order Primate age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  lazy val australopithecusAge = 4 *: my // Some("genus Australopithecus age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))
  //  lazy val modernHumanAge = 200 *: ky // Some("anatomically modern human age"), None, Some("http://en.wikipedia.org/wiki/Timeline_of_evolution"))

}
