package axle.ml

import org.scalatest._

import shapeless._
import spire.random.Generator

class GeneticAlgorithmSpec extends FunSuite with Matchers {

  case class Rabbit(a: Int, b: Double, c: Double, d: Double, e: Double, f: Double, g: Double, h: Double)

  test("Genetic Algorithm: build a better rabbit") {

    val generic = Generic[Rabbit]

    def randomRabbit(rng: Generator): generic.Repr = {
      val rabbit = Rabbit(
        1 + rng.nextInt(2),
        5 + 20 * rng.nextDouble(),
        1 + 4 * rng.nextDouble(),
        3 + 10 * rng.nextDouble(),
        10 + 5 * rng.nextDouble(),
        2 + 2 * rng.nextDouble(),
        3 + 5 * rng.nextDouble(),
        2 + 10 * rng.nextDouble())
      generic.to(rabbit)
    }

    def fitness(rg: generic.Repr): Double = {
      val rabbit = generic.from(rg)
      import rabbit._
      rabbit.a * 100 + 100.0 * b + 2.2 * (1.1 * c + 0.3 * d) + 1.3 * (1.4 * e - 3.1 * f + 1.3 * g) - 1.4 * h
    }

    // import spire.implicits._

    val ga = GeneticAlgorithm(
      randomRabbit,
      fitness,
      populationSize = 100,
      numGenerations = 100)

    val log = ga.run()

    val winner = log.winners.last

    log.maxs should have size 100
    fitness(winner) should be > 0d
  }

}
