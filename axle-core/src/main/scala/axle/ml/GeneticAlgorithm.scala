package axle.ml

import scala.collection.immutable.TreeMap
import shapeless._
import shapeless.ops.hlist.Mapper
import shapeless.ops.hlist.Zip
import spire.random.Generator
import axle.poly._


trait Species[G] {

  def random(gen: Generator): G

  def fitness(genotype: G): Double

}

case class GeneticAlgorithmLog[G](
  winners: IndexedSeq[G],
  mins: TreeMap[Int, Double],
  maxs: TreeMap[Int, Double],
  aves: TreeMap[Int, Double])

case class GeneticAlgorithm[G <: HList, Z <: HList](
  populationSize: Int = 1000,
  numGenerations: Int = 100)(
    implicit species: Species[G],
    zipper: Zip.Aux[G :: G :: HNil, Z],
    mapperMixer: Mapper[Mixer1.type, Z],
    mapperMutate: Mapper[Mutator1.type, Z]
    ) {

  def initialPopulation(gen: Generator): IndexedSeq[(G, Double)] =
    (0 until populationSize).map(i => { 
      val r = species.random(gen)
      (r, species.fitness(r))
    })

  def mate[X <: HList](r: G, m: G, f: G)(
      implicit zipper: Zip.Aux[G :: G :: HNil, X],
      mapperMixer: Mapper[Mixer1.type, X],
      mapperMutator: Mapper[Mutator1.type, X]) = {
    val mixed = ((m zip f) map Mixer1).asInstanceOf[G] // TODO
    ((r zip mixed) map Mutator1).asInstanceOf[G] // TODO
  }

  def live(
      population: IndexedSeq[(G, Double)],
      fitnessLog: List[(Double, Double, Double)])(
      gen: Generator): (IndexedSeq[(G, Double)], List[(Double, Double, Double)]) = {

    val nextGen = (0 until populationSize).map(i => {
      val (m1, m1f) = population(gen.nextInt(population.size))
      val (m2, m2f) = population(gen.nextInt(population.size))
      val (f, _) = population(gen.nextInt(population.size))
      val m = if (m1f > m2f) { m1 } else { m2 }
      val kid = mate(m, f, species.random(gen)).asInstanceOf[G]
      (kid, species.fitness(kid))
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
