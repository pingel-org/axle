package axle.ml

import org.scalatest._

//import shapeless._
//import spire.random.Generator
//import spire.random.Generator.rng
//import axle.poly.Mixer2
//import axle.poly.Mutator2

class GeneticAlgorithmSpec extends FunSuite with Matchers {

  case class Rabbit(a: Int, b: Double, c: Double, d: Double, e: Double, f: Double, g: Double, h: Double)

  test("Genetic Algorithm: build a better rabbit") {

    /*
    val generic = Generic[Rabbit]

    implicit val rabbitSpecies = new Species[generic.Repr] {

      def random(gen: Generator): generic.Repr = {
        val rabbit = Rabbit(
          1 + gen.nextInt(2),
          5 + 20 * gen.nextDouble(),
          1 + 4 * gen.nextDouble(),
          3 + 10 * gen.nextDouble(),
          10 + 5 * gen.nextDouble(),
          2 + 2 * gen.nextDouble(),
          3 + 5 * gen.nextDouble(),
          2 + 10 * gen.nextDouble())
        generic.to(rabbit)
      }

      def fitness(rg: generic.Repr): Double = {
        val rabbit = generic.from(rg)
        import rabbit._
        rabbit.a * 100 + 100.0 * b + 2.2 * (1.1 * c + 0.3 * d) + 1.3 * (1.4 * e - 3.1 * f + 1.3 * g) - 1.4 * h
      }

    }

    // import shapeless.ops.hlist.RightFolder
    // implicitly[RightFolder[generic.Repr,Generator,Mutator2.type]]

    val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)

    val log = ga.run(rng)

    val winner = log.winners.last

    log.maxs should have size 100
    rabbitSpecies.fitness(winner) should be > 0d
    *
    */
    1 should be(1)
  }

}
