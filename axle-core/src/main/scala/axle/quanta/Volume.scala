package axle.quanta

import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import spire.algebra.Eq
import spire.algebra.Field

case class Volume[N]() extends Quantum4[N] {

  type Q = Volume[N]

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Volume"

  def unit(name: String, symbol: String, wiki: Option[String] = None) =
    UnitOfMeasurement4[Q, N](name, symbol, wiki)

  def m3: UnitOfMeasurement4[Q, N] = unit("m3", "m3") // derive
  def km3: UnitOfMeasurement4[Q, N] = unit("km3", "km3") // derive
  def cm3: UnitOfMeasurement4[Q, N] = unit("cm3", "cm3") // derive
  def greatLakes: UnitOfMeasurement4[Q, N] = unit("Great Lakes Volume", "Great Lakes Volume", Some("http://en.wikipedia.org/wiki/Great_Lakes"))
  def liter: UnitOfMeasurement4[Q, N] = unit("liter", "L", Some("http://en.wikipedia.org/wiki/Liter")) // TODO: also symbol ℓ
  def L = liter
  def milliliter: UnitOfMeasurement4[Q, N] = unit("milliliter", "mL")

  def wineBottle: UnitOfMeasurement4[Q, N] = unit("wine bottle", "wineBottle", Some("http://en.wikipedia.org/wiki/Wine_bottle"))
  def magnum: UnitOfMeasurement4[Q, N] = unit("magnum", "magnum")
  def jeroboam: UnitOfMeasurement4[Q, N] = unit("jeroboam", "jeroboam")
  def rehoboam: UnitOfMeasurement4[Q, N] = unit("rehoboam", "rehoboam")
  def methuselah: UnitOfMeasurement4[Q, N] = unit("methuselah", "methuselah")
  def salmanazar: UnitOfMeasurement4[Q, N] = unit("salmanazar", "salmanazar")
  def balthazar: UnitOfMeasurement4[Q, N] = unit("balthazar", "balthazar")
  def nebuchadnezzar: UnitOfMeasurement4[Q, N] = unit("nebuchadnezzar", "nebuchadnezzar")

  def units: List[UnitOfMeasurement4[Q, N]] =
    List(m3, km3, cm3, greatLakes, liter, milliliter, wineBottle, magnum, jeroboam, rehoboam,
      methuselah, salmanazar, balthazar, nebuchadnezzar)

  def links(implicit fn: Field[N]): Seq[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])] =
    List[(UnitOfMeasurement4[Q, N], UnitOfMeasurement4[Q, N], Bijection[N, N])](
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