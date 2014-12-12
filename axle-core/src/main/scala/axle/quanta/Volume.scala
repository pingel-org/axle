package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

//class Volume[DG[_, _]: DirectedGraph] extends Quantum {
//
//  def wikipediaUrl = "http://en.wikipedia.org/wiki/Volume"
//
//  type Q = this.type
//
//  def units[N: Field: Eq] = List[UnitOfMeasurement[Q, N]](
//    unit("m3", "m3"), // derive
//    unit("km3", "km3"), // derive
//    unit("cm3", "cm3"), // derive
//    unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes")),
//    unit("liter", "L", Some("http://en.wikipedia.org/wiki/Liter")), // TODO: also symbol ℓ
//    unit("milliliter", "mL"),
//    unit("wine bottle", "wineBottle", Some("http://en.wikipedia.org/wiki/Wine_bottle")),
//    unit("magnum", "magnum"),
//    unit("jeroboam", "jeroboam"),
//    unit("rehoboam", "rehoboam"),
//    unit("methuselah", "methuselah"),
//    unit("salmanazar", "salmanazar"),
//    unit("balthazar", "balthazar"),
//    unit("nebuchadnezzar", "nebuchadnezzar"))
//
//  def links[N: Field: Eq] = {
//    implicit val baseCG = cgnDisconnected[N, DG]
//    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
//      (km3, greatLakes, ScaleInt(22671)),
//      (milliliter, liter, Scale10s(3)),
//      (cm3, milliliter, BijectiveIdentity[N]),
//      (milliliter, wineBottle, ScaleInt(750)),
//      (wineBottle, magnum, ScaleInt(2)),
//      (wineBottle, jeroboam, ScaleInt(4)),
//      (wineBottle, rehoboam, ScaleInt(6)),
//      (wineBottle, methuselah, ScaleInt(8)),
//      (wineBottle, salmanazar, ScaleInt(12)),
//      (wineBottle, balthazar, ScaleInt(16)),
//      (wineBottle, nebuchadnezzar, ScaleInt(20)))
//  }
//
//  def m3[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "m3")
//  def km3[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "km3")
//  def cm3[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "cm3")
//  def milliliter[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "cm3")
//  def greatLakes[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "Great Lakes Volume")
//  def liter[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "liter")
//  def L[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "liter")
//  def mL[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "milliter")
//
//  def wineBottle[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "wine bottle")
//  def magnum[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "magnum")
//  def jeroboam[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "jeroboam")
//  def rehoboam[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "rehoboam")
//  def methuselah[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "methuselah")
//  def salmanazar[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "salmanazar")
//  def balthazar[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "balthazar")
//  def nebuchadnezzar[N: Field: Eq](implicit cg: CG[DG, N]) = byName(cg, "nebuchadnezzar")
//
//}

case class Volume() extends Quantum("http://en.wikipedia.org/wiki/Orders_of_magnitude_(time)")

object Volume {

  type Q = Volume

  def unit[N](name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement[Q, N](name, symbol, wiki)

  def m3[N]: UnitOfMeasurement[Q, N] = unit("m3", "m3") // derive
  def km3[N]: UnitOfMeasurement[Q, N] = unit("km3", "km3") // derive
  def cm3[N]: UnitOfMeasurement[Q, N] = unit("cm3", "cm3") // derive
  def greatLakes[N]: UnitOfMeasurement[Q, N] = unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes"))
  def liter[N]: UnitOfMeasurement[Q, N] = unit("liter", "L", Some("http://en.wikipedia.org/wiki/Liter")) // TODO: also symbol ℓ
  def milliliter[N]: UnitOfMeasurement[Q, N] = unit("milliliter", "mL")
  def wineBottle[N]: UnitOfMeasurement[Q, N] = unit("wine bottle", "wineBottle", Some("http://en.wikipedia.org/wiki/Wine_bottle"))
  def magnum[N]: UnitOfMeasurement[Q, N] = unit("magnum", "magnum")
  def jeroboam[N]: UnitOfMeasurement[Q, N] = unit("jeroboam", "jeroboam")
  def rehoboam[N]: UnitOfMeasurement[Q, N] = unit("rehoboam", "rehoboam")
  def methuselah[N]: UnitOfMeasurement[Q, N] = unit("methuselah", "methuselah")
  def salmanazar[N]: UnitOfMeasurement[Q, N] = unit("salmanazar", "salmanazar")
  def balthazar[N]: UnitOfMeasurement[Q, N] = unit("balthazar", "balthazar")
  def nebuchadnezzar[N]: UnitOfMeasurement[Q, N] = unit("nebuchadnezzar", "nebuchadnezzar")

  def units[N]: List[UnitOfMeasurement[Q, N]] =
    List(m3, km3, cm3, greatLakes, liter, milliliter, wineBottle, magnum, jeroboam, rehoboam,
      methuselah, salmanazar, balthazar, nebuchadnezzar)

  def links[N: Field]: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])](
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

  implicit def conversionGraph[N: Field: Eq, DG[_, _]: DirectedGraph] =
    Quantum.cgn(units[N], links)

}