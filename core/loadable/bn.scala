
import collection._
import axle.stats._
import axle.visualize._

val bools = Some(Vector(true, false))

val B = RandomVariable0("Burglary", bools)
val E = RandomVariable0("Earthquake", bools)
val A = RandomVariable0("Alarm", bools)
val J = RandomVariable0("John Calls", bools)
val M = RandomVariable0("Mary Calls", bools)

val bn = BayesianNetwork("Alarm sounds (due to Burglary or Earthquake) and John or Mary Call")

val bv = bn += BayesianNetworkNode(B,
  Factor(Vector(B), Map(
    List(B eq true) -> 0.001,
    List(B eq false) -> 0.999
  )))

val ev = bn += BayesianNetworkNode(E,
  Factor(Vector(E), Map(
    List(E eq true) -> 0.002,
    List(E eq false) -> 0.998
  )))

val av = bn += BayesianNetworkNode(A,
  Factor(Vector(B, E, A), Map(
    List(B eq false, E eq false, A eq true) -> 0.001,
    List(B eq false, E eq false, A eq false) -> 0.999,
    List(B eq true, E eq false, A eq true) -> 0.94,
    List(B eq true, E eq false, A eq false) -> 0.06,
    List(B eq false, E eq true, A eq true) -> 0.29,
    List(B eq false, E eq true, A eq false) -> 0.71,
    List(B eq true, E eq true, A eq true) -> 0.95,
    List(B eq true, E eq true, A eq false) -> 0.05
  )))

val jv = bn += BayesianNetworkNode(J,
  Factor(Vector(A, J), Map(
    List(A eq true, J eq true) -> 0.9,
    List(A eq true, J eq false) -> 0.1,
    List(A eq false, J eq true) -> 0.05,
    List(A eq false, J eq false) -> 0.95
  )))

val mv = bn += BayesianNetworkNode(M,
  Factor(Vector(A, M), Map(
    List(A eq true, M eq true) -> 0.7,
    List(A eq true, M eq false) -> 0.3,
    List(A eq false, M eq true) -> 0.01,
    List(A eq false, M eq false) -> 0.99
  )))

bn += (bv -> av, "")
bn += (ev -> av, "")
bn += (av -> jv, "")
bn += (av -> mv, "")

