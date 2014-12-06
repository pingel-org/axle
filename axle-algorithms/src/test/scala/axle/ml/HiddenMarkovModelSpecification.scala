package axle.ml

import org.specs2.mutable._

import axle.jung.JungDirectedGraph.directedGraphJung


class HiddenMarkovModelSpecification extends Specification {

  val rainy = UnobservableMarkovModelState("Rainy")
  val sunny = UnobservableMarkovModelState("Sunny")
  val walk = ObservableMarkovModelState("walk")
  val shop = ObservableMarkovModelState("shop")
  val clean = ObservableMarkovModelState("clean")

  val hmm = HiddenMarkovModel(
    Vector(rainy, sunny),
    Vector(walk, shop, clean),
    Map(rainy -> 0.6, sunny -> 0.4),
    Map(
      rainy -> Map(rainy -> 0.7, sunny -> 0.3),
      sunny -> Map(rainy -> 0.4, sunny -> 0.6)),
    Map(
      rainy -> Map(walk -> 0.1, shop -> 0.4, clean -> 0.5),
      sunny -> Map(walk -> 0.6, shop -> 0.3, clean -> 0.1)))

}
