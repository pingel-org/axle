package axle.ml

import scala.collection.immutable.TreeMap
import shapeless._
import shapeless.ops.hlist.RightFolder
import shapeless.ops.hlist.Zip
import spire.random.Generator
import axle.poly._

/**

  Non-HList approximation of the "mate" function:

  type GA[T] = (Generator, List[T])

  def mix[T](current: (T, T), genAcc: GA[T]): GA[T] = {
    val (gen, acc) = genAcc
    val choice = if (gen.nextBoolean()) current._1 else current._2
    (gen, choice :: acc)
  }

  def mutate[T](current: (T, T), genAcc: GA[T]): GA[T] = {
    val (gen, acc) = genAcc
    val choice = if (gen.nextDouble() < 0.03) current._1 else current._2
    (gen, choice :: acc)
  }

  import spire.random.Generator.rng

  val m = List(1, 8, 11)
  val f = List(2, 3, 12)
  val r = List(7, 7, 7)

  val mixed = m.zip(f).foldRight((rng, List.empty[Int]))(mix)._2
  val mated = r.zip(mixed).foldRight((rng, List.empty[Int]))(mutate)._2

 */

case class GeneticAlgorithmLog[G](
  winners: IndexedSeq[G],
  mins: TreeMap[Int, Double],
  maxs: TreeMap[Int, Double],
  aves: TreeMap[Int, Double])

case class GeneticAlgorithm[G <: HList, Z <: HList](
  random: Generator => G,
  fitness: G => Double,
  populationSize: Int = 1000,
  numGenerations: Int = 100)(
    implicit zipper: Zip.Aux[G :: G :: HNil, Z],
    folderMixer: RightFolder[Z, Generator, Mixer2.type],
    folderMutate: RightFolder[Z, Generator, Mutator2.type]
    ) {

  def initialPopulation(gen: Generator): IndexedSeq[(G, Double)] =
    (0 until populationSize).map(i => { 
      val r = random(gen)
      (r, fitness(r))
    })

  def mate[X <: HList](m: G, f: G, gen: Generator)(
      implicit zipper: Zip.Aux[G :: G :: HNil, X],
      folderMixer: RightFolder[X, Generator, Mixer2.type],
      folderMutator: RightFolder[X, Generator, Mutator2.type]) = {

    val r: G = random(gen)

    val mixed = ((m zip f).foldRight(gen)(Mixer2)(folderMixer)).asInstanceOf[G] // TODO
    ((r zip mixed).foldRight(gen)(Mutator2)).asInstanceOf[G] // TODO
  }

  def live(
      population: IndexedSeq[(G, Double)],
      fitnessLog: List[(Double, Double, Double)])(
      gen: Generator): (IndexedSeq[(G, Double)], List[(Double, Double, Double)]) = {

    val nextGen = (0 until populationSize).map(i => {

      val (male1, m1f) = population(gen.nextInt(population.size))
      val (male2, m2f) = population(gen.nextInt(population.size))
      val male = if (m1f > m2f) { male1 } else { male2 }

      val (f, _) = population(gen.nextInt(population.size))

      val kid = mate(male, f, gen).asInstanceOf[G]
      (kid, fitness(kid))
    })

    (nextGen, minMaxAve(nextGen) :: fitnessLog)
  }

  def minMaxAve(population: IndexedSeq[(G, Double)]): (Double, Double, Double) =
    (population.minBy(_._2)._2, population.maxBy(_._2)._2, population.map(_._2).sum / population.size)

  def run(gen: Generator): GeneticAlgorithmLog[G] = {

    val populationLog = (0 until numGenerations)
      .foldLeft((initialPopulation(gen), List[(Double, Double, Double)]()))(
        (pl: (IndexedSeq[(G, Double)], List[(Double, Double, Double)]), i: Int) => live(pl._1, pl._2)(gen))

    val winners = populationLog._1.reverse.map(_._1)
    val logs = populationLog._2.reverse
    val mins = new TreeMap[Int, Double]() ++ (0 until logs.size).map(i => (i, logs(i)._1))
    val maxs = new TreeMap[Int, Double]() ++ (0 until logs.size).map(i => (i, logs(i)._2))
    val aves = new TreeMap[Int, Double]() ++ (0 until logs.size).map(i => (i, logs(i)._3))

    GeneticAlgorithmLog[G](winners, mins, maxs, aves)
  }

}
