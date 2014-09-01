package axle.quanta2

import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Field
import spire.algebra.Eq
import spire.implicits.moduleOps
import spire.math.Real
import spire.math.Rational
import spire.math.Rational.apply
import axle.graph.DirectedGraph
import axle.graph.Vertex
import spire.algebra.Field
import spire.algebra.Eq
import spire.math.Rational
import spire.math.Real
import spire.implicits.eqOps
import spire.implicits.moduleOps
import spire.implicits.groupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits._

class Distance extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)"
}

object Distance extends Distance {

  def cgdn[N: Field: Eq]: DirectedGraph[UnitOfMeasurement[Distance, N], N => N] = conversions(
    List(
      unit("foot", "ft"),
      unit("mile", "m", Some("http://en.wikipedia.org/wiki/Mile")),
      unit("meter", "m"),
      unit("kilometer", "km"),
      unit("centimeter", "cm"),
      unit("millimeter", "mm"),
      unit("micrometer", "μm"),
      unit("nanometer", "nm"),
      unit("Astronomical Unit", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
      unit("Astronomical Unit (SI)", "AU", Some("http://en.wikipedia.org/wiki/Astronomical_unit")),
      unit("light year", "ly", Some("http://en.wikipedia.org/wiki/Light-year")),
      unit("parsec", "pc", Some("http://en.wikipedia.org/wiki/Parsec"))),
    (vs: Seq[Vertex[UnitOfMeasurement[Distance, N]]]) => vs match {
      case ft :: mile :: meter :: km :: cm :: mm :: μm :: nm :: au :: ausi :: ly :: pc :: Nil => List( //        (ft, mile, 5280),
      //        (km, mile, 1.609344),
      //        (meter, km, 1E3),
      //        (cm, meter, 1E2),
      //        (mm, meter, 1E3),
      //        (μm, meter, 1E6),
      //        (nm, meter, 1E9),
      //        (mile, au, 92955807.3),
      //        (km, ausi, 149597870.7),
      //        (km, ly, 9460730472580.8),
      //        (ly, pc, 3.26)
      )
      case _ => Nil
    })

  implicit val cgDRational: DirectedGraph[UnitOfMeasurement[Distance, Rational], Rational => Rational] = cgdn[Rational]
  implicit val cgDReal: DirectedGraph[UnitOfMeasurement[Distance, Real], Real => Real] = cgdn[Real]
  implicit val cgDDouble: DirectedGraph[UnitOfMeasurement[Distance, Double], Double => Double] = cgdn[Double]
  implicit val cgDFloat: DirectedGraph[UnitOfMeasurement[Distance, Float], Float => Float] = cgdn[Float]

  implicit val mtRational = modulize[Distance, Rational]
  implicit val mtReal = modulize[Distance, Real]
  implicit val mtDouble = modulize[Distance, Double]
  implicit val mtFloat = modulize[Distance, Float]

  def cm[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Distance, N], N => N]) = byName(cg, "cm")
  def meter[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Distance, N], N => N]) = byName(cg, "meter")
  def foot[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Distance, N], N => N]) = byName(cg, "foot")
  def ft[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Distance, N], N => N]) = byName(cg, "foot")
  def mile[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Distance, N], N => N]) = byName(cg, "mile")
  def parsec[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Distance, N], N => N]) = byName(cg, "parsec")
  def lightyear[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[UnitOfMeasurement[Distance, N], N => N]) = byName(cg, "lightyear")

  //  lazy val foot = byName(cgDR, "foot")
  //  lazy val ft = foot
  //  lazy val mile = byName(cgDR, "mile")
  //  lazy val meter = byName(cgDR, "meter")
  //  lazy val kilometer = byName(cgDR, "kilometer")
  //  lazy val km = kilometer
  //  lazy val centimeter = byName(cgDR, "centimeter")
  //  lazy val cm = centimeter
  //  lazy val millimeter = byName(cgDR, "millimeter")
  //  lazy val mm = millimeter
  //  lazy val micrometer = byName(cgDR, "micrometer")
  //  lazy val μm = micrometer
  //  lazy val nanometer = byName(cgDR, "nanometer")
  //  lazy val nm = nanometer
  //  lazy val au = byName(cgDR, "Astronomical Unit")
  //  lazy val auSI = byName(cgDR, "Astronomical Unit (SI)")
  //  lazy val lightyear = byName(cgDR, "light year")
  //  lazy val parsec = byName(cgDR, "parsec")
  //  
  //  lazy val ny2LA = Rational("2443.79") *: mile // Some("NY to LA"), None, Some("http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html"))
  //  lazy val milkyWayDiameter = Rational(100000) *: lightyear // Some("Milky Way Diameter"), None, Some("http://en.wikipedia.org/wiki/Milky_Way"))
  //  lazy val toAndromeda = Rational(2.6E6) *: lightyear // Some("Distance to Andromeda"), None, Some("http://en.wikipedia.org/wiki/Andromeda_Galaxy")))))))))))))

}
