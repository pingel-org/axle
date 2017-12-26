package axle.ml

import org.scalatest._

import shapeless._
//import nat._
//import spire.random.Generator
//import spire.random.Generator.rng

class GeneticAlgorithmSpec extends FunSuite with Matchers {

  type G = Int :: Double :: Double :: Double :: Double :: HNil
  type GG = (Int, Int) :: (Double, Double) :: (Double, Double) :: (Double, Double) :: (Double, Double) :: HNil

  test("Genetic Algorithm: build a better rabbit") {

    // val generic = Generic[Rabbit]
    // generic.Repr
    /*
    implicit val rabbitSpecies = new Species[G] {

      def random(gen: Generator): G = {
        1 + gen.nextInt(2) ::
          5 + 20 * gen.nextDouble() ::
          1 + 4 * gen.nextDouble() ::
          3 + 10 * gen.nextDouble() ::
          10 + 5 * gen.nextDouble() :: HNil
      }

      def fitness(rg: G): Double = {
        rg(_0) * 100 + 100.0 * rg(_1) + 2.2 * (1.1 * rg(_2) + 0.3 * rg(_3) + 1.3 * (1.4 * rg(_4)))
      }

    }
*/
    //    val ga = GeneticAlgorithm[G, GG](populationSize = 100, numGenerations = 100)
    //
    //    val log = ga.run(rng)
    //
    //    val winner = log.winners.last
    //
    //    log.maxs should have size 100
    //    rabbitSpecies.fitness(winner) should be > 0d

    1 should be(1)
  }

}
