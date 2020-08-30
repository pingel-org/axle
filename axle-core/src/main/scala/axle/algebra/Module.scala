package axle.algebra

import spire.algebra.Rng

trait Module[T, U] {

    def negate(x: T): T

    def zero: T

    def plus(x: T, y: T): T

    implicit def scalar: Rng[T]

    def timesl(r: U, v: T): T

    def timesr(v: T, l: U): T

}
