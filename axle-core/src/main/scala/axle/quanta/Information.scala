package axle.quanta

import axle.algebra.Vertex
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational
import spire.math.Real
//import spire.implicits._

class Information[DG[_, _]: DirectedGraph] extends Quantum {

  def wikipediaUrl = "http://en.wikipedia.org/wiki/Information"

  type Q = this.type

  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
    unit("bit", "b"),
    unit("nibble", "nibble"),
    unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte")),
    unit("kilobyte", "KB"),
    unit("megabyte", "MB"),
    unit("gigabyte", "GB"),
    unit("terabyte", "TB"),
    unit("petabyte", "PB"))

  def links[N: Field: Eq] = {
    implicit val baseCG = cgnDisconnected[N, DG]
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
      (bit, byte, Scale2s(3)),
      (byte, kilobyte, Scale2s(10)),
      (kilobyte, megabyte, Scale2s(10)),
      (megabyte, gigabyte, Scale2s(10)),
      (gigabyte, terabyte, Scale2s(10)),
      (terabyte, petabyte, Scale2s(10)))
  }

  //  implicit val cgIRational = cgn[Rational]
  //  implicit val mtRational = modulize[Information, Rational]

  def bit[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "bit")
  def nibble[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "nibble")
  def byte[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "byte")
  def kilobyte[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "kilobyte")
  def KB[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "KB")
  def megabyte[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "megabyte")
  def MB[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "MB")
  def gigabyte[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "gigabyte")
  def GB[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "GB")
  def terabyte[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "terabyte")
  def TB[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "TB")
  def petabyte[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "petabyte")
  def PB[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "PB")

}

case class Information3() extends Quantum3

object Information3 extends Quantum3 {

  def unit[N: Field: Eq](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement3[Information3, N](name, symbol, wiki)

  def bit[N: Field: Eq] = unit("bit", "b")
  def nibble[N: Field: Eq] = unit("nibble", "nibble")
  def byte[N: Field: Eq] = unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte"))
  def kilobyte[N: Field: Eq] = unit("kilobyte", "KB")
  def megabyte[N: Field: Eq] = unit("megabyte", "MB")
  def gigabyte[N: Field: Eq] = unit("gigabyte", "GB")
  def terabyte[N: Field: Eq] = unit("terabyte", "TB")
  def petabyte[N: Field: Eq] = unit("petabyte", "PB")

  def units[N: Field: Eq]: List[UnitOfMeasurement3[Information3, N]] =
    List(bit, nibble, byte, kilobyte, megabyte, gigabyte, terabyte, petabyte)

  def links[N: Field: Eq]: Seq[(UnitOfMeasurement3[Information3, N], UnitOfMeasurement3[Information3, N], Bijection[N, N])] =
    List[(UnitOfMeasurement3[Information3, N], UnitOfMeasurement3[Information3, N], Bijection[N, N])](
      (bit, byte, Scale2s(3)),
      (byte, kilobyte, Scale2s(10)),
      (kilobyte, megabyte, Scale2s(10)),
      (megabyte, gigabyte, Scale2s(10)),
      (gigabyte, terabyte, Scale2s(10)),
      (terabyte, petabyte, Scale2s(10)))

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum3.cgn(units, links)
      
}
