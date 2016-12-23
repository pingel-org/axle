package axle.ml

import org.scalatest._

import shapeless._
import util.Random.nextDouble
import util.Random.nextInt

class GeneticAlgorithmSpec extends FunSuite with Matchers {

  case class Rabbit(a: Int, b: Double, c: Double, d: Double, e: Double, f: Double, g: Double, h: Double)

  test("Genetic Algorithm: build a better rabbit") {

    val gen = Generic[Rabbit]

    // val pMutation = 0.003

    implicit val rabbitSpecies = new Species[gen.Repr] {

      def random() = {
        val rabbit = Rabbit(
          1 + nextInt(2),
          5 + 20 * nextDouble(),
          1 + 4 * nextDouble(),
          3 + 10 * nextDouble(),
          10 + 5 * nextDouble(),
          2 + 2 * nextDouble(),
          3 + 5 * nextDouble(),
          2 + 10 * nextDouble())
        gen.to(rabbit)
      }

      def fitness(rg: gen.Repr): Double = {
        val rabbit = gen.from(rg)
        import rabbit._
        rabbit.a * 100 + 100.0 * b + 2.2 * (1.1 * c + 0.3 * d) + 1.3 * (1.4 * e - 3.1 * f + 1.3 * g) - 1.4 * h
      }

    }

    // import spire.implicits._

    val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)

    val log = ga.run()

    val winner = log.winners.last

    log.maxs should have size 100
    rabbitSpecies.fitness(winner) should be > 0d
  }

}
