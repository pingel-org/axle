package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field
import spire.math.Rational

case class Volume() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volume"

}

trait VolumeUnits[N] {

  type U = UnitOfMeasurement[Volume, N]

  def m3: U
  def km3: U
  def cm3: U
  def greatLakes: U
  def liter: U
  def L: U
  def milliliter: U

  def wineBottle: U
  def magnum: U
  def jeroboam: U
  def rehoboam: U
  def methuselah: U
  def salmanazar: U
  def balthazar: U
  def nebuchadnezzar: U

}

trait VolumeMetadata[N] extends QuantumMetadata[Volume, N] with VolumeUnits[N]

object Volume {

  def metadata[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new QuantumMetadataGraph[Volume, N, DG] with VolumeMetadata[N] {

      def unit(name: String, symbol: String, wiki: Option[String] = None) =
        UnitOfMeasurement[Volume, N](name, symbol, wiki)

      lazy val _m3 = unit("m3", "m3") // derive
      lazy val _km3 = unit("km3", "km3") // derive
      lazy val _cm3 = unit("cm3", "cm3") // derive
      lazy val _greatLakes = unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes"))
      lazy val _liter = unit("liter", "L", Some("http://en.wikipedia.org/wiki/Liter")) // TODO: also symbol ℓ
      lazy val _milliliter = unit("milliliter", "mL")

      lazy val _wineBottle = unit("wine bottle", "wineBottle", Some("http://en.wikipedia.org/wiki/Wine_bottle"))
      lazy val _magnum = unit("magnum", "magnum")
      lazy val _jeroboam = unit("jeroboam", "jeroboam")
      lazy val _rehoboam = unit("rehoboam", "rehoboam")
      lazy val _methuselah = unit("methuselah", "methuselah")
      lazy val _salmanazar = unit("salmanazar", "salmanazar")
      lazy val _balthazar = unit("balthazar", "balthazar")
      lazy val _nebuchadnezzar = unit("nebuchadnezzar", "nebuchadnezzar")

      def m3 = _m3
      def km3 = _km3
      def cm3 = _cm3
      def greatLakes = _greatLakes
      def liter = _liter
      def L = _liter
      def milliliter = _milliliter

      def wineBottle = _wineBottle
      def magnum = _magnum
      def jeroboam = _jeroboam
      def rehoboam = _rehoboam
      def methuselah = _methuselah
      def salmanazar = _salmanazar
      def balthazar = _balthazar
      def nebuchadnezzar = _nebuchadnezzar

      def units: List[UnitOfMeasurement[Volume, N]] =
        List(m3, km3, cm3, greatLakes, liter, milliliter, wineBottle, magnum, jeroboam, rehoboam,
          methuselah, salmanazar, balthazar, nebuchadnezzar)

      def links: Seq[(UnitOfMeasurement[Volume, N], UnitOfMeasurement[Volume, N], Bijection[N, N])] =
        List[(UnitOfMeasurement[Volume, N], UnitOfMeasurement[Volume, N], Bijection[N, N])](
          (km3, greatLakes, ScaleInt(22671)),
          (milliliter, liter, Scale10s(3)),
          (cm3, milliliter, BijectiveIdentity[N]),
          (milliliter, wineBottle, ScaleInt(750)),
          (wineBottle, magnum, ScaleInt(2)),
          (wineBottle, jeroboam, ScaleInt(4)),
          (wineBottle, rehoboam, ScaleInt(6)),
          (wineBottle, methuselah, ScaleInt(8)),
          (wineBottle, salmanazar, ScaleInt(12)),
          (wineBottle, balthazar, ScaleInt(16)),
          (wineBottle, nebuchadnezzar, ScaleInt(20)))

    }
}