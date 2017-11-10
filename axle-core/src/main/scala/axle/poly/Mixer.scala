package axle.poly

import shapeless._

import spire.random.Generator

object Mix extends Poly1 {
  implicit def casePair[A] =
    at[(A, A)] { aa: (A, A) => (g: Generator) =>
      if (g.nextBoolean) aa._1 else aa._2
    }
}
