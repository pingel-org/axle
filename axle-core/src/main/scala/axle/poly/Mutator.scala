package axle.poly

import shapeless._

import spire.random.Generator

object Mutate extends Poly1 {
  implicit def casePair[A] =
    at[(A, A)] { aa: (A, A) => (g: Generator) =>
      if (g.nextDouble() < 0.03) aa._1 else aa._2
    }
}
