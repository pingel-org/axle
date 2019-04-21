package axle.quantumcircuit

import axle.algebra.Binary
import axle.algebra.tensorProduct

case class DiracVector(s: String, parser: String => Vector[Binary]) {

  def cbits: Vector[CBit] = parser(s).map(CBit.fromBinary)

  def unindex: Vector[Binary] =
    parser(s)
     .map(CBit.fromBinary)
     .map(_.unindex)
     .reduce({ (xs, ys) => tensorProduct(xs, ys) })

}

object DiracVector {

  def binaryStringToVectorBinary(s: String): Vector[Binary] =
    s.split("").map(_.toInt).map(Binary.fromInt).toVector

  def intStringToVectorBinary(s: String): Vector[Binary] =
    s.toInt.toBinaryString.split("").map(_.toInt).map(Binary.fromInt).toVector

}

case class OpenDiracVector(s: String, parser: String => Vector[Binary]) {
  def >(): DiracVector = DiracVector(s, parser)
}

object | {

  def apply(
    s: String,
    parser: String => Vector[Binary] = DiracVector.binaryStringToVectorBinary): OpenDiracVector =
    OpenDiracVector(s, parser)

}
