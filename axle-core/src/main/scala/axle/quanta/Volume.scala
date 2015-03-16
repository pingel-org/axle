package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Volume() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volume"

}

trait VolumeUnits extends QuantumUnits[Volume] {

  lazy val m3 = unit("m3", "m3") // derive
  lazy val km3 = unit("km3", "km3") // derive
  lazy val cm3 = unit("cm3", "cm3") // derive
  lazy val greatLakes = unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes"))
  lazy val liter = unit("liter", "L", Some("http://en.wikipedia.org/wiki/Liter")) // TODO: also symbol ℓ
  lazy val L = liter
  lazy val milliliter = unit("milliliter", "mL")

  lazy val wineBottle = unit("wine bottle", "wineBottle", Some("http://en.wikipedia.org/wiki/Wine_bottle"))
  lazy val magnum = unit("magnum", "magnum")
  lazy val jeroboam = unit("jeroboam", "jeroboam")
  lazy val rehoboam = unit("rehoboam", "rehoboam")
  lazy val methuselah = unit("methuselah", "methuselah")
  lazy val salmanazar = unit("salmanazar", "salmanazar")
  lazy val balthazar = unit("balthazar", "balthazar")
  lazy val nebuchadnezzar = unit("nebuchadnezzar", "nebuchadnezzar")

  def units: List[UnitOfMeasurement[Volume]] =
    List(m3, km3, cm3, greatLakes, liter, milliliter, wineBottle, magnum, jeroboam, rehoboam,
      methuselah, salmanazar, balthazar, nebuchadnezzar)

}

trait VolumeConverter[N] extends UnitConverter[Volume, N] with VolumeUnits

object Volume {

  def converterGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    new UnitConverterGraph[Volume, N, DG] with VolumeConverter[N] {

      def links: Seq[(UnitOfMeasurement[Volume], UnitOfMeasurement[Volume], Bijection[N, N])] =
        List[(UnitOfMeasurement[Volume], UnitOfMeasurement[Volume], Bijection[N, N])](
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