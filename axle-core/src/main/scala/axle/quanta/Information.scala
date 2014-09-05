package axle.quanta

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
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], N => N, N => N)]()
  }
  //trips2fns(List(
  //          (bit, byte, 8),
  //          (byte, kilobyte, 1024),
  //          (kilobyte, megabyte, 1024),
  //          (megabyte, gigabyte, 1024),
  //          (gigabyte, terabyte, 1024),
  //          (terabyte, petabyte, 1024))
  //          )

  //  implicit val cgIRational = cgn[Rational]
  //  implicit val mtRational = modulize[Information, Rational]

  def bit[N: Field: Eq](implicit cg: CG[N]) = byName(cg, "bit")

  //  lazy val bit = byName(cgIR, "bit")
  //  lazy val nibble = byName(cgIR, "nibble")
  //  lazy val byte = byName(cgIR, "byte")
  //  lazy val kilobyte = byName(cgIR, "kilobyte")
  //  lazy val megabyte = byName(cgIR, "megabyte")
  //  lazy val gigabyte = byName(cgIR, "gigabyte")
  //  lazy val terabyte = byName(cgIR, "terabyte")
  //  lazy val petabyte = byName(cgIR, "petabyte")
  //
  //  lazy val KB = kilobyte
  //  lazy val MB = megabyte
  //  lazy val GB = gigabyte
  //  lazy val TB = terabyte
  //  lazy val PB = petabyte

}
