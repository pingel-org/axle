package axle.quanta

import axle.graph.DirectedGraph
import axle.graph.Vertex
import axle.algebra.Bijection
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

abstract class Information extends Quantum {
  def wikipediaUrl = "http://en.wikipedia.org/wiki/Information"
}

object Information extends Information {

  import spire.implicits._

  type Q = Information

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
    implicit val baseCG = cgnDisconnected[N]
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

  def bit[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "bit")
  def nibble[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "nibble")
  def byte[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "byte")
  def kilobyte[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "kilobyte")
  def KB[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "KB")
  def megabyte[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "megabyte")
  def MB[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "MB")
  def gigabyte[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "gigabyte")
  def GB[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "GB")
  def terabyte[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "terabyte")
  def TB[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "TB")
  def petabyte[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "petabyte")
  def PB[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "PB")

}
