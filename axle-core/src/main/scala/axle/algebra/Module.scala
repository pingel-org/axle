package axle.algebra

import spire.algebra.Rng

trait Module[T, S] {

    def negate(x: T): T

    def zero: T

    def plus(x: T, y: T): T

    implicit def scalar: Rng[S]

    def timesl(s: S, v: T): T

    def timesr(v: T, s: S): T = timesl(s, v)

}
