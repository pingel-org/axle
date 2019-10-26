package axle.pgm

import org.scalatest._
import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.implicits._
import spire.math._

import axle.stats._
import axle.jung.directedGraphJung

class AlarmBurglaryEarthquakeSpec extends FunSuite with Matchers {

  implicit val showRat = cats.Show.fromToString[Rational]

  val bools = Vector(true, false)

  val B = Variable[Boolean]("Burglary")
  val E = Variable[Boolean]("Earthquake")
  val A = Variable[Boolean]("Alarm")
  val J = Variable[Boolean]("John Calls")
  val M = Variable[Boolean]("Mary Calls")

  val bFactor =
    Factor(Vector(B -> bools), Map(
      Vector(B is true) -> Rational(1, 1000),
      Vector(B is false) -> Rational(999, 1000)))

  val eFactor =
    Factor(Vector(E -> bools), Map(
      Vector(E is true) -> Rational(1, 500),
      Vector(E is false) -> Rational(499, 500)))

  val aFactor =
    Factor(Vector(B -> bools, E -> bools, A -> bools), Map(
      Vector(B is false, E is false, A is true) -> Rational(1, 1000),
      Vector(B is false, E is false, A is false) -> Rational(999, 1000),
      Vector(B is true, E is false, A is true) -> Rational(940, 1000),
      Vector(B is true, E is false, A is false) -> Rational(60, 1000),
      Vector(B is false, E is true, A is true) -> Rational(290, 1000),
      Vector(B is false, E is true, A is false) -> Rational(710, 1000),
      Vector(B is true, E is true, A is true) -> Rational(950, 1000),
      Vector(B is true, E is true, A is false) -> Rational(50, 1000)))

  val jFactor =
    Factor(Vector(A -> bools, J -> bools), Map(
      Vector(A is true, J is true) -> Rational(9, 10),
      Vector(A is true, J is false) -> Rational(1, 10),
      Vector(A is false, J is true) -> Rational(5, 100),
      Vector(A is false, J is false) -> Rational(95, 100)))

  val mFactor =
    Factor(Vector(A -> bools, M -> bools), Map(
      Vector(A is true, M is true) -> Rational(7, 10),
      Vector(A is true, M is false) -> Rational(3, 10),
      Vector(A is false, M is true) -> Rational(1, 100),
      Vector(A is false, M is false) -> Rational(99, 100)))

  // edges: ba, ea, aj, am
  val bn: BayesianNetwork[Boolean, Rational, DirectedSparseGraph[BayesianNetworkNode[Boolean, Rational], Edge]] =
    BayesianNetwork.withGraphK2[Boolean, Rational, DirectedSparseGraph](
      "A sounds (due to Burglary or Earthquake) and John or Mary Call",
      Map(
        B -> bFactor,
        E -> eFactor,
        A -> aFactor,
        J -> jFactor,
        M -> mFactor))

  test("bayesian networks produces a Joint Probability Table, which is '1' when all variables are removed") {

    val jpt = bn.jointProbabilityTable

    val sansAll: Factor[Boolean, Rational] = jpt.Σ(M).Σ(J).Σ(A).Σ(B).Σ(E)

    import spire.implicits.multiplicativeSemigroupOps
    (bn.factorFor(A) * bn.factorFor(B)) * bn.factorFor(E) // dropping "abe"

    // val Q: Set[Variable[Boolean]] = Set(E, B, A)
    // val order = List(J, M)

    // val afterVE = bn.variableEliminationPriorMarginalI(Q, order)
    // val afterVE = bn.variableEliminationPriorMarginalII(Q, order, E is true)
    // bn.getDistributions.map(rv => println(bn.getMarkovAssumptionsFor(rv)))
    // println("P(B) = " + ans1) // 0.001
    // println("P(A| B, -E) = " + ans2) // 0.94
    // println("eliminating variables other than A, B, and E; and then finding those consistent with E = true")
    // println(afterVE)

    sansAll.apply(Vector.empty) should be(Rational(1))
    sansAll.evaluate(Seq.empty, Seq.empty) should be(Rational(1))
  }

  test("bayesian network visualization") {

    import axle.jung._
    import axle.visualize._
    import axle.awt._
    import axle.web._

    val pngGName = "gnGraph.png"
    val svgGName = "gnGraph.svg"
    val graphVis = DirectedGraphVisualization[DirectedSparseGraph[BayesianNetworkNode[Boolean, Rational], Edge], BayesianNetworkNode[Boolean, Rational] ](
      bn.graph, 200, 200, 10)
    png(graphVis, pngGName)
    svg(graphVis, svgGName)

    val pngName = "bn.png"
    val svgName = "bn.svg"
    val vis = BayesianNetworkVisualization[Boolean, Rational, DirectedSparseGraph[BayesianNetworkNode[Boolean, Rational], Edge]](bn, 200, 200, 10)
    png(vis, pngName)
    svg(vis, svgName)

    new java.io.File(pngGName).exists should be(true)
    new java.io.File(pngName).exists should be(true)
    new java.io.File(svgGName).exists should be(true)
    new java.io.File(svgName).exists should be(true)

  }
}
