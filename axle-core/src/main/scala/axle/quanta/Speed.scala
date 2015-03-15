package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Speed() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Speed"

}

trait SpeedUnits[N] {

  type U = UnitOfMeasurement[Speed, N]

  def mps: U
  def fps: U
  def mph: U
  def kph: U
  def knot: U
  def kn: U
  def c: U
  def speedLimit: U
}

trait SpeedMetadata[N] extends QuantumMetadata[Speed, N] with SpeedUnits[N]

object Speed {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Speed, N, DG] with SpeedMetadata[N] {

      def unit(name: String, symbol: String, wiki: Option[String] = None) =
        UnitOfMeasurement[Speed, N](name, symbol, wiki)

      lazy val _mps = unit("mps", "mps") // derive
      lazy val _fps = unit("fps", "fps") // derive
      lazy val _mph = unit("mph", "mph") // derive
      lazy val _kph = unit("kph", "kph") // derive
      lazy val _knot = unit("knot", "kn", Some("http://en.wikipedia.org/wiki/Knot_(unit)"))
      lazy val _c = unit("Light Speed", "c", Some("http://en.wikipedia.org/wiki/Speed_of_light"))
      lazy val _speedLimit = unit("Speed limit", "speed limit")

      def mps = _mps
      def fps = _fps
      def mph = _mph
      def kph = _kph
      def knot = _knot
      def kn = _knot
      def c = _c
      def speedLimit = _speedLimit

      def units: List[UnitOfMeasurement[Speed, N]] =
        List(mps, fps, mph, kph, knot, c, speedLimit)

      def links: Seq[(UnitOfMeasurement[Speed, N], UnitOfMeasurement[Speed, N], Bijection[N, N])] =
        List[(UnitOfMeasurement[Speed, N], UnitOfMeasurement[Speed, N], Bijection[N, N])](
          (knot, kph, ScaleDouble(1.852)),
          (mps, c, ScaleInt(299792458)),
          (mph, speedLimit, ScaleInt(65)))

    }

}