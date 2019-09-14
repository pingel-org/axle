package axle.stats

import cats.Show
import cats.kernel.Order
//import cats.Order.catsKernelOrderingForOrder
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.Generator

import axle.dummy
import axle.math.Σ
import axle.algebra.Region
import axle.algebra.RegionEq
//import axle.algebra.RegionSet

object ConditionalProbabilityTable {

  implicit def showCPT[A: Show: Order, V: Show: Field](implicit prob: ProbabilityModel[ConditionalProbabilityTable], showRA: Show[Region[A]]): Show[ConditionalProbabilityTable[A, V]] = cpt =>
    cpt.regions.map { regionA => { // TODO regions.sorted
      val aString = showRA.show(regionA)
      (aString + (1 to (cpt.charWidth - aString.length)).map(i => " ").mkString("") + " " + Show[V].show(prob.probabilityOfExpression(cpt)(regionA)))
    }}.mkString("\n")

  // ({ type λ[V] = ConditionalProbabilityTable[A, V] })#λ
  implicit val probabilityWitness: ProbabilityModel[ConditionalProbabilityTable] =
    new ProbabilityModel[ConditionalProbabilityTable] {

      // def construct[A, V](variable: Variable[A], universe: Region[A], f: Region[A] => V)(implicit ring: Ring[V], eqA: cats.kernel.Eq[A]): ConditionalProbabilityTable[A, V] =
      //   ConditionalProbabilityTable[A, V](partition.map(ra => ra -> f(ra)).toMap, variable)

      // def regions[A](model: ConditionalProbabilityTable[A, _]): Iterable[Region[A]] =
      //   model.regions // .map(regionA => (w: A) => (model.eqA.eqv(w, regionA)))

      def adjoin[A, V1, V2](model: ConditionalProbabilityTable[A, V1])(other: ConditionalProbabilityTable[A, V2])(implicit eqA: cats.kernel.Eq[A], fieldV1: Field[V1], fieldV2: Field[V2], eqV1: cats.kernel.Eq[V1], eqV2: cats.kernel.Eq[V2]): ConditionalProbabilityTable[A, (V1, V2)] = {

        val newValues = (model.p.keySet ++ other.p.keySet) // TODO should unique by Eq[A], not universal equality

        implicit val fieldV12: Field[(V1, V2)] = axle.algebra.tuple2Field[V1, V2](fieldV1, fieldV2, eqV1, eqV2)

        ConditionalProbabilityTable[A, (V1, V2)](
          newValues.map( key => {
            val valuePair = (probabilityOfExpression(model)(key), probabilityOfExpression(other)(key))
            key -> valuePair
          }).toMap,
          //  RegionSet(newValues),
          // { a: Region[A] => (probabilityOfExpression(model)(a), probabilityOfExpression(other)(a)) },
          model.variable)
      }

      def chain[A, B, V]
        (model: ConditionalProbabilityTable[A, V])
        (other: ConditionalProbabilityTable[B, V])
        (implicit fieldV: Field[V], eqA: cats.kernel.Eq[A], eqB: cats.kernel.Eq[B]): ConditionalProbabilityTable[(A, B), V] = {

        val abvMap: Map[RegionEq[(A, B)], V] = (
          for {
            regionA <- model.regions
            regionB <- other.regions
          } yield {
            val partitionAB: RegionEq[(A, B)] = RegionEq((regionA.x, regionB.x)) // .combine(regionA, regionB)
            val v: V = probabilityOfExpression(model)(regionA) * probabilityOfExpression(other)(regionB)
            partitionAB -> v
          }
        ).toMap

        ConditionalProbabilityTable[(A, B), V](
          abvMap,
          Variable[(A, B)](model.variable.name + " " + other.variable.name))
      }

      def mapValues[A, V, V2](model: ConditionalProbabilityTable[A, V])(f: V => V2)(implicit fieldV: Field[V], ringV2: Ring[V2]): ConditionalProbabilityTable[A, V2] = {
        import model.eqA
        ConditionalProbabilityTable[A, V2](
          model.p.mapValues(f),
          model.variable)
      }

      def filter[A, V](model: ConditionalProbabilityTable[A, V])(predicate: A => Boolean)(implicit fieldV: Field[V]): ConditionalProbabilityTable[A, V] = {
        val newMap: Map[RegionEq[A], V] = model.p.toVector.filter({ case (req, v) => predicate(req.x)}).groupBy(_._1).map( bvs => bvs._1 -> Σ(bvs._2.map(_._2)) )
        val newDenominator: V = Σ(newMap.values)
        import model.eqA
        ConditionalProbabilityTable[A, V](newMap.mapValues(v => v / newDenominator), model.variable)
      }

      def empty[A, V](variable: Variable[A])(implicit eqA: cats.kernel.Eq[A], ringV: Ring[V]): ConditionalProbabilityTable[A, V] = {
        ConditionalProbabilityTable(Map.empty, variable)
      }

      def observe[A, V](model: ConditionalProbabilityTable[A, V])(gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A = {
        val r: V = gen.next[V]
        model.bars.find({ case (_, v) => orderV.gteqv(v, r) }).get._1 // otherwise malformed distribution
      }

      // def probabilityOf[A, V](model: ConditionalProbabilityTable[A, V])(a: A)(implicit fieldV: Field[V]): V =
      //   model.p.get(a).getOrElse(fieldV.zero)

      def probabilityOfExpression[A, V](model: ConditionalProbabilityTable[A, V])(predicate: Region[A])(implicit fieldV: Field[V]): V =
        predicate match {
          case RegionEq(b) =>
            Σ(model.regions.map( r => r match {
              case re @ RegionEq(a) => if (model.eqA.eqv(a, b)) { model.p(re) } else { fieldV.zero }
              case _ => ???
            }))
          case _ => ???
        }
  }

}

case class ConditionalProbabilityTable[A, V](
  p:        Map[RegionEq[A], V],
  variable: Variable[A])(implicit ringV: Ring[V], val eqA: cats.kernel.Eq[A]) {

  val bars = p.toVector.scanLeft((dummy[A], ringV.zero))((x, y) => (y._1.x, x._2 + y._2)).drop(1)

  def regions: Iterable[RegionEq[A]] = p.keys.toVector

  def charWidth(implicit showRA: Show[Region[A]]): Int =
    (regions.map(ra => showRA.show(ra).length).toList).reduce(math.max)

}
