# Future Work

* `{CPT,TD}.tailRecM` then ScalaCheck `Monad[CPT,TD]`
* Functor for CPT, TD

* `SigmaAlgebra` for the CPT
  * Clean up expressions like `RegionIf[TWOROLLS](_._1 == '⚃)`
  * Laws for `Region` ("Sigma Algebra"? [video](https://www.youtube.com/watch?v=21a85f1YS5Q))
  * `OrderedRegion` for the `Order` used in `RegionLTE` and `RegionGTE`?

* Measure Theory

* Test: start with `ABE.jointProbabilityTable` (monotype `tuple5[Boolean]`)
  * Factor out each variable until original 5-note network is reached
  * Basically the inverse of factor multiplication
  * `bn.factorFor(B) * bn.factorFor(E)` should be defined? (It errors)
  * `MonotypeBayesanNetwork.filter` collapase into a single BNN

* Rename `ConditionalProbabilityTable`?

* Laws for `Factor`

* Review `InteractionGraph`, `EliminationGraph`, `JoinTree` and the functions they power

* Consider a "case" to be a `Map` vs a `Vector`
* Consider usefulness of `Factor` in terms of `Region`

* `MonotypeBayesanNetwork`.{`pure`, `map`, `flatMap`, `tailRecR`}
* Reconcile `MBN` `combine1` & `combine2`
* Monad tests for `MonotypeBayesanNetwork[Alarm-Burglary-Earthquake]`

* `Bayes[MonotypeBayesanNetwork]` -- could be viewed as "belief updating" (vs "conditioning")
  * If it took a ProbabilityModel itself
