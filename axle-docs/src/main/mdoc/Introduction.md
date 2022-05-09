# Introduction

## Objectives

Practice coding in a strongly functional style and writing about it

No doubles (easy path to "theorems for free")

Lawful AI

## Gallery

[![mandelbrot](/images/mandelbrot.png)](ChaosTheory.md#mandelbrot-set)

[![coin entropy](/images/coinentropy.svg)](RandomnessUncertainty.md#example-entropy-of-a-biased-coin)

[![alarm bayes](/images/alarm_bayes.svg)](RandomnessUncertainty.md#bayesian-networks)

[![logistic map](/images/logMap.png)](ChaosTheory.md#logistic-map)

[![poker hands](/images/poker_hands.svg)](GameTheory.md#poker)

[![probability monad](/images/iffy.svg)](RandomnessUncertainty.md#probability-model)

[![cluster irises](/images/k_means.svg)](MachineLearning.md#example-irises)

[![linear regression](/images/lrerror.svg)](MachineLearning.md#linear-regression)

[![geo coordinates](/images/sfo_hel_small.png)](Units.md#geo-coordinates)

[![random waves](/images/random_waves.svg)](Visualization.md#example-plot-random-waves-over-time)

## Installation

Axle as a dependency of an SBT project.

### Install SBT

See [SBT](http://www.scala-sbt.org/)

### Create SBT Project from Giter8 Template

```bash
sbt new axlelang/axle.g8
```

(Less commonly used `axle-laws`, `axle-awt`, and `axle-parallel` are not included in seed project.)

In addition to the axle-lang.org jars are several other third party jars.
Axle is compiled with these jars in `provided` scope,
meaning that they are compiled and packaged with the expectation that the user of the Axle
jars will explicitly provide those dependencies.

### Next Steps

Run `sbt console` to launch the Scala REPL with the Axle jars in the classpath.
Axle works well interactively -- especially during prototyping, debugging, and testing.
Any of the Axle tutorials can be copied and pasted into the REPL.

To start writing code, check out `src/main/scala/example/Hello.scala`,
and go from there.

### Releases

@RELEASE_VERSION@ is the most recent released version:

See the [Road Map](Appendix.md#road-map) for more information on the release schedule.

### Snapshots

Snapshot versions are created for every commit and
hosted on the [Sonatype snapshot repo](https://oss.sonatype.org/content/repositories/snapshots).

## Community Resources

* Chat on the gitter channel: [![gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/axlelang/axle?utm_source=badge)
* [@axledsl](https://twitter.com/axledsl) Twitter handle
