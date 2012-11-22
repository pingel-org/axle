package axle.quanta

import axle.graph.JungDirectedGraph._
import java.math.BigDecimal
import math.{ max, abs }
import collection._

/**
 * Quantum
 *
 * Used in the sense of the World English Dictionary's 4th definition:
 *
 * 4. something that can be quantified or measured
 *
 * [[http://dictionary.reference.com/browse/quantum]]
 *
 */

/**
 * TODO
 *
 * HSet for numerator + HSet for denominator ?
 * name, symbol, link for new units resulting from by and over
 * derive should heck that the given compound unit is in this quantum's list of derivations?
 * derive should add the compoundUnit to the graph?
 * reconcile newEdge(source, dest) and newEdge(source, dest, magnitude)
 */

trait Quantum {

  outer =>

  type UOM <: UnitOfMeasurement

  val conversionGraph: JungDirectedGraph[UOM, BigDecimal] = null // TODO

  //  type CGE = JungDirectedGraphEdge[UOM, BigDecimal] // conversionGraph.type#E
  //  type CGV = JungDirectedGraphVertex[UOM] // conversionGraph.type#V

  implicit def toBD(i: Int) = new BigDecimal(i.toString)

  implicit def toBD(d: Double) = new BigDecimal(d.toString)

  implicit def toBD(s: String) = new BigDecimal(s)

  def bdDivide(numerator: BigDecimal, denominator: BigDecimal) = numerator.divide(
    denominator,
    max(max(numerator.precision, abs(numerator.scale)),
      max(denominator.precision, abs(denominator.scale))),
    java.math.RoundingMode.HALF_UP)

  val oneBD = new BigDecimal("1")
  val zeroBD = new BigDecimal("0")

  trait UnitOfMeasurement {

    self: UOM =>

    def conversion(): Option[JungDirectedGraphEdge[UOM, BigDecimal]]
    // def update(cge: JungDirectedGraphEdge[UOM, BigDecimal]): Unit
    def label(): String
    def symbol(): Option[String]
    def link(): Option[String]

    def +(right: UOM): UOM
    def -(right: UOM): UOM
    def *(bd: BigDecimal): UOM
    def /(bd: BigDecimal): UOM

    def *:(bd: BigDecimal): UOM

    def magnitudeIn(u: UOM): BigDecimal

    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#UOM
    def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM
    def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM
    def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM
    def in(other: UOM): UOM
  }

  trait ZeroWithUnit extends UnitOfMeasurement {

    self: UOM =>

    // override def update(cge: JungDirectedGraphEdge[UOM, BigDecimal]): Unit = {}

    override def +(right: UOM): UOM = right
    override def -(right: UOM): UOM = right * -1.0
    override def *(bd: BigDecimal): UOM = self
    override def /(bd: BigDecimal): UOM = self

    override def *:(bd: BigDecimal) = self
    // def in_:(bd: BigDecimal) = quantity(bd, this) // How would this be defined on the zero?
    override def magnitudeIn(u: UOM): BigDecimal = zeroBD

    // TODO: remove nulls
    override def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#UOM = null.asInstanceOf[QRES#UOM]
    override def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = null.asInstanceOf[QRES#UOM]
    override def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = null.asInstanceOf[QRES#UOM]
    override def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = null.asInstanceOf[QRES#UOM]
    override def in(other: UOM): UOM = null.asInstanceOf[UOM]

  }

  class UnitOfMeasurementImpl(
    var _conversion: Option[JungDirectedGraphEdge[UOM, BigDecimal]],
    _name: Option[String] = None,
    _symbol: Option[String] = None,
    _link: Option[String] = None) extends UnitOfMeasurement {

    self: UOM =>

    val quantum: Quantum = outer

    //    val vertex = conversionGraph += this
    //    uom2vertex += this -> vertex

    def conversion() = _conversion
    // def update(cge: JungDirectedGraphEdge[UOM, BigDecimal]) = _conversion = Some(cge)
    def label() = _name.getOrElse("")
    def symbol() = _symbol
    def link() = _link

    def kilo() = quantity(oneBD.scaleByPowerOfTen(3), this, Some("kilo" + _name.getOrElse("")), Some("K" + symbol.getOrElse("")))
    def mega() = quantity(oneBD.scaleByPowerOfTen(6), this, Some("mega" + _name.getOrElse("")), Some("M" + symbol.getOrElse("")))
    def giga() = quantity(oneBD.scaleByPowerOfTen(9), this, Some("giga" + _name.getOrElse("")), Some("G" + symbol.getOrElse("")))
    def tera() = quantity(oneBD.scaleByPowerOfTen(12), this, Some("kilo" + _name.getOrElse("")), Some("T" + symbol.getOrElse("")))
    def peta() = quantity(oneBD.scaleByPowerOfTen(15), this, Some("peta" + _name.getOrElse("")), Some("P" + symbol.getOrElse("")))
    def exa() = quantity(oneBD.scaleByPowerOfTen(18), this, Some("exa" + _name.getOrElse("")), Some("E" + symbol.getOrElse("")))
    def zetta() = quantity(oneBD.scaleByPowerOfTen(21), this, Some("zetta" + _name.getOrElse("")), Some("Z" + symbol.getOrElse("")))
    def yotta() = quantity(oneBD.scaleByPowerOfTen(24), this, Some("yotta" + _name.getOrElse("")), Some("Y" + symbol.getOrElse("")))

    def deci() = quantity(oneBD.scaleByPowerOfTen(-1), this, Some("deci" + _name.getOrElse("")), Some("d" + symbol.getOrElse("")))
    def centi() = quantity(oneBD.scaleByPowerOfTen(-2), this, Some("centi" + _name.getOrElse("")), Some("c" + symbol.getOrElse("")))
    def milli() = quantity(oneBD.scaleByPowerOfTen(-3), this, Some("milli" + _name.getOrElse("")), Some("m" + symbol.getOrElse("")))
    def micro() = quantity(oneBD.scaleByPowerOfTen(-6), this, Some("micro" + _name.getOrElse("")), Some("μ" + symbol.getOrElse("")))
    def nano() = quantity(oneBD.scaleByPowerOfTen(-9), this, Some("nano" + _name.getOrElse("")), Some("n" + symbol.getOrElse("")))

    override def toString() = _name.getOrElse(_conversion
      .map(c => c.payload + " " + c.source.payload.symbol.getOrElse(""))
      .getOrElse(_name.getOrElse("") + " (" + symbol.getOrElse("") + "): a measure of " + this.getClass().getSimpleName()))

    def +(right: UOM): UOM = {
      val (bd, uom) = _conversion.map(c => (c.payload, c.source.payload)).getOrElse((oneBD, this))
      quantity(bd.add((right in uom).conversion.get.payload), uom) // TODO remove .get
    }

    def -(right: UOM): UOM = {
      val (bd, uom) = _conversion.map(c => (c.payload, c.source.payload)).getOrElse((oneBD, this))
      quantity(bd.subtract((right in uom).conversion.get.payload), uom) // TODO remove .get
    }

    def *(bd: BigDecimal): UOM = bd.doubleValue match {
      case 0.0 => zero()
      case _ => _conversion
        .map(c => quantity(c.payload.multiply(bd), c.source.payload))
        .getOrElse(quantity(bd, this))
    }

    def /(bd: BigDecimal): UOM = _conversion
      .map(c => quantity(bdDivide(c.payload, bd), c.source.payload))
      .getOrElse(quantity(bdDivide(oneBD, bd), this))

    override def *:(bd: BigDecimal) = bd.doubleValue match {
      case 0.0 => zero()
      case _ => quantity(bd, this)
    }

    def in_:(bd: BigDecimal) = quantity(bd, this)

    //    def magnitudeIn(u: UOM): BigDecimal = if (getConversion.get == u) {
    //      getConversion.get.getPayload
    //    } else {
    //      val otherVertex = vertexFor(u)
    //      val thisVertex = vertexFor(this)
    //      conversionGraph.shortestPath(otherVertex, thisVertex).map(path => {
    //        path.foldLeft(oneBD)((bd: BigDecimal, edge: CGE) => bd.multiply(edge.getPayload))
    //      }).getOrElse(throw new Exception("no conversion path from " + this + " to " + u))
    //    }

    def magnitudeIn(u: UOM): BigDecimal =
      conversionGraph.shortestPath(vertexFor(u), vertexFor(this)).map(path => {
        path.foldLeft(oneBD)((bd: BigDecimal, edge: DirectedGraphEdge[UOM, BigDecimal]) => bd.multiply(edge.payload))
      }).getOrElse(throw new Exception("no conversion path from " + this + " to " + u))

    def by[QRGT <: Quantum, QRES <: Quantum](right: QRGT#UOM, resultQuantum: QRES): QRES#UOM = {
      val resultBD = _conversion.map(c =>
        right.conversion.map(rc => c.payload.multiply(rc.payload)
        ).getOrElse(c.payload)
      ).getOrElse(right.conversion.map(_.payload)
        .getOrElse(oneBD)
      )
      resultQuantum.quantity(resultBD, resultQuantum.newUnitOfMeasurement(None))
    }

    def over[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = {
      val resultBD = _conversion.map(c =>
        bottom.conversion.map(bc => bdDivide(c.payload, bc.payload)
        ).getOrElse(c.payload)
      ).getOrElse(bottom.conversion.map(bc => bdDivide(oneBD, bc.payload))
        .getOrElse(oneBD)
      )
      resultQuantum.quantity(resultBD, resultQuantum.newUnitOfMeasurement(None))
    }

    def through[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = over(bottom, resultQuantum)

    def per[QBOT <: Quantum, QRES <: Quantum](bottom: QBOT#UOM, resultQuantum: QRES): QRES#UOM = over(bottom, resultQuantum)

    def in(other: UOM): UOM = {
      val otherVertex = vertexFor(other)
      val thisVertex = vertexFor(this)
      val resultBD = conversionGraph.shortestPath(otherVertex, thisVertex).map(path => {
        path.foldLeft(oneBD)((bd: BigDecimal, edge: DirectedGraphEdge[UOM, BigDecimal]) => bd.multiply(edge.payload))
      })
      if (resultBD.isEmpty) {
        throw new Exception("no conversion path from " + this + " to " + other)
      }
      quantity(resultBD.get, other)
    }

  }

  def zero(): UOM

  def newUnitOfMeasurement(
    conversion: Option[JungDirectedGraphEdge[UOM, BigDecimal]] = None,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): UOM

  def unit(name: String, symbol: String, linkOpt: Option[String] = None): UOM =
    newUnitOfMeasurement(None, Some(name), Some(symbol), linkOpt)

  def derive(compoundUnit: UnitOfMeasurement,
    nameOpt: Option[String] = None,
    symbolOpt: Option[String] = None,
    linkOpt: Option[String] = None): UOM =
    newUnitOfMeasurement(None, nameOpt, symbolOpt, linkOpt)

  //  def link(base: UOM, multiple: BigDecimal, result: UOM): Unit = {
  //    val baseVertex = vertexFor(base)
  //    val resultVertex = vertexFor(result)
  //    conversionGraph += (resultVertex -> baseVertex, multiple)
  //    conversionGraph += (baseVertex -> resultVertex, bdDivide(oneBD, multiple))
  //  }

  val uom2vertex = Map[UOM, JungDirectedGraphVertex[UOM]]()

  def vertexFor(uom: UOM): JungDirectedGraphVertex[UOM] = uom2vertex(uom)

  def quantity(
    magnitude: BigDecimal,
    unit: UOM,
    qname: Option[String] = None,
    qsymbol: Option[String] = None,
    qlink: Option[String] = None): UOM = {
    // TODO (mutually recursive?) (magnitude.doubleValue == 0.0) match { case true => zero()
    val uom = newUnitOfMeasurement(None, qname, qsymbol, qlink)
    val uomVertex = vertexFor(uom)
    val unitVertex = vertexFor(unit)
    // TODO
    //    val conversion1 = conversionGraph += (uomVertex -> unitVertex, bdDivide(oneBD, magnitude))
    //    val conversion2 = conversionGraph += (unitVertex -> uomVertex, magnitude)
    //    uom() = conversion2
    uom
  }

  val wikipediaUrl: String

  //  val derivations: List[Quantum]
  //
  //  def by(right: Quantum, resultQuantum: Quantum): Quantum = QuantumMultiplication(this, right, resultQuantum)
  //
  //  def over(bottom: Quantum, resultQuantum: Quantum): Quantum = QuantumMultiplication(this, bottom, resultQuantum)

  override def toString() = this.getClass().getSimpleName()

  /**
   * Searches the Directed Graph defined by this Quantum for a path of Conversions from source to goal
   * @param source Start node for shortest path search
   * @param goal End node for shortest path search
   */
  //  def conversionPath(source: UOM, goal: UOM): Option[List[Conversion]] =
  //    shortestPath(source.asInstanceOf[V], goal.asInstanceOf[V])

}

/**
case class QuantumMultiplication[QLEFT <: Quantum, QRIGHT <: Quantum, QRESULT <: Quantum](left: QLEFT, right: QRIGHT, resultQuantum: QRESULT) extends Quantum {

  type UOM = QRESULT#UOM

  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil
  val derivations = Nil
  val examples = Nil
  
  def newUnitOfMeasurement(
    baseUnit: Option[QRESULT#UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): UOM = {

    resultQuantum.newUnitOfMeasurement(None, magnitude, name, symbol, link)
  }
  
}

case class QuantumDivision[QTOP <: Quantum, QBOTTOM <: Quantum, QRESULT <: Quantum](top: QTOP, bottom: QBOTTOM, resultQuantum: QRESULT) extends Quantum {

  type UOM = QRESULT#UOM

  val wikipediaUrl = ""
  val unitsOfMeasurement = Nil
  val derivations = Nil
  val examples = Nil
  
  def newUnitOfMeasurement(
    baseUnit: Option[UOM] = None,
    magnitude: BigDecimal,
    name: Option[String] = None,
    symbol: Option[String] = None,
    link: Option[String] = None): UOM = {

    resultQuantum.newUnitOfMeasurement(None, magnitude, name, symbol, link)
  }
  
}
*/

/**
  object ConversionGraphFactoryObject extends ConversionGraphFactory
  trait ConversionGraphFactory extends JungDirectedGraphFactory {
    
    type G = ConversionGraph
    
    trait ConversionGraph extends JungDirectedGraph[UOM, BigDecimal] {

      type V = ConversionGraphVertex
      trait ConversionGraphVertex extends JungDirectedGraphVertex[UOM]

      type E = ConversionGraphEdge
      trait ConversionGraphEdge extends JungDirectedGraphEdge[BigDecimal]
    }
  }
  import ConversionGraphFactoryObject._
 * 
 * 
 */
