package axle.quanta2

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

class Information extends Quantum {
  def wikipediaUrl = "TODO"
}

object Information extends Information {

  import spire.implicits._

  implicit def cgin[N: Field: Eq]: DirectedGraph[Quantity[Information, N], N => N] = conversions(
    List(
      unit("bit", "b"),
      unit("nibble", "nibble"),
      unit("byte", "B", Some("http://en.wikipedia.org/wiki/Byte")),
      unit("kilobyte", "KB"),
      unit("megabyte", "MB"),
      unit("gigabyte", "GB"),
      unit("terabyte", "TB"),
      unit("petabyte", "PB")),
    (vs: Seq[Vertex[Quantity[Information, N]]]) => vs match {
      case bit :: nibble :: byte :: kilobyte :: megabyte :: gigabyte :: terabyte :: petabyte :: Nil =>
        (bit, nibble, (b: N) => b * 4) ::
        (nibble, bit, (n: N) => n / 4) ::
        Nil
      case _ => Nil
    })

  //trips2fns(List(
  //          (bit, byte, 8),
  //          (byte, kilobyte, 1024),
  //          (kilobyte, megabyte, 1024),
  //          (megabyte, gigabyte, 1024),
  //          (gigabyte, terabyte, 1024),
  //          (terabyte, petabyte, 1024))
  //          )

  implicit val cgIRational: DirectedGraph[Quantity[Information, Rational], Rational => Rational] = cgin[Rational]
  implicit val cgIReal: DirectedGraph[Quantity[Information, Real], Real => Real] = cgin[Real]

  implicit val mtRational = modulize[Information, Rational]
  implicit val mtReal = modulize[Information, Real]

  def bit[N](implicit fieldN: Field[N], eqN: Eq[N], cg: DirectedGraph[Quantity[Information, N], N => N]) = byName(cg, "bit")

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
