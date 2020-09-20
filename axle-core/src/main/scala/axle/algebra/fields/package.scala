package axle.algebra

import spire.algebra._

package object fields {

  implicit def tuple2Field[V1, V2](implicit fieldV1: Field[V1], fieldV2: Field[V2], eqV1: cats.kernel.Eq[V1], eqV2: cats.kernel.Eq[V2]): Field[(V1, V2)] =
  new Field[(V1, V2)] {

    // Members declared in algebra.ring.AdditiveGroup
    def negate(x: (V1, V2)): (V1, V2) =
      (fieldV1.negate(x._1), fieldV2.negate(x._2))
  
    // Members declared in algebra.ring.AdditiveMonoid
    def zero: (V1, V2) =
      (fieldV1.zero, fieldV2.zero)
  
    // Members declared in algebra.ring.AdditiveSemigroup
    def plus(x: (V1, V2),y: (V1, V2)): (V1, V2) =
      (fieldV1.plus(x._1, y._1), fieldV2.plus(x._2, y._2))
  
    // Members declared in spire.algebra.GCDRing
    def gcd(a: (V1, V2),b: (V1, V2))(implicit ev: spire.algebra.Eq[(V1, V2)]): (V1, V2) =
      (fieldV1.gcd(a._1, b._1), fieldV2.gcd(a._2, b._2))

    def lcm(a: (V1, V2),b: (V1, V2))(implicit ev: spire.algebra.Eq[(V1, V2)]): (V1, V2) =
      (fieldV1.lcm(a._1, b._1), fieldV2.lcm(a._2, b._2))
  
    // Members declared in algebra.ring.MultiplicativeGroup
    def div(x: (V1, V2),y: (V1, V2)): (V1, V2) =
      (fieldV1.div(x._1, y._1), fieldV2.div(x._2, y._2))

    // Members declared in algebra.ring.MultiplicativeSemigroup
    def times(x: (V1, V2),y: (V1, V2)): (V1, V2) =
      (fieldV1.times(x._1, y._1), fieldV2.times(x._2, y._2))

    // MultiplicativeMonoid
    def one: (V1, V2) =
      (fieldV1.one, fieldV2.one)

  }    
}
