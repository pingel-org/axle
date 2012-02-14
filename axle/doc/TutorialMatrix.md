
Matrix
======

I would eventually like to cover some of the same kinds of expressions in these
Octave and Matlab tutorials:

1. http://volga.eng.yale.edu/sohrab/matlab_tutorial.html
1. http://en.wikibooks.org/wiki/Octave_Programming_Tutorial
1. http://www-mdp.eng.cam.ac.uk/web/CD/engapps/octave/octavetut.pdf

```scala
import org.pingel.axle.matrix._
import org.pingel.axle.matrix.DoubleJblasMatrixFactory._
```

A REPL session is the best way to show some basic functionality:

```scala
scala> val x = rand(1, 3)
x: org.pingel.axle.matrix.DoubleJblasMatrixFactory.Matrix = 0.7284942995365215 0.3462185685770226 0.026266114979279664

scala> x.t
res5: org.pingel.axle.matrix.DoubleJblasMatrixFactory.Matrix = 
0.7284942995365215
0.3462185685770226
0.026266114979279664

scala> x * 4
res6: org.pingel.axle.matrix.DoubleJblasMatrixFactory.Matrix = 2.913977198146086 1.3848742743080904 0.10506445991711866
```

There are unicode and ascii versions of many operators.

See the [Machine Learning Tutorial](https://github.com/adampingel/pingel.org/blob/master/axle/doc/TutorialMachineLearning.md)
for more interesting examples.



