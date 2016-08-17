
package axle.site.chapter

import axle.site._
import axle.blog.model._
import ExternalResources._

object Algebra {

  val intro =
    <div>
<p>The { spire.link } project is a dependency of Axle.
<code>spire.algebra</code> defines typeclasses for Monoid, Group, Ring, Field, VectorSpace, etc, and
witnesses for many common numeric types as well as those defined in <code>spire.math</code></p>
<p>
The <code>axle.algebra</code> package defines several categories of typeclasses:
<ul>
<li>higher-kinded: Functor, Finite, Indexed, Aggregatable</li>
<li>mathematical: LinearAlgebra, LengthSpace</li>
<li>visualization: Tics, Plottable</li>
</ul>
Axioms are defined in the <a href="https://github.com/adampingel/axle/tree/master/axle-core/src/main/scala/axle/algebra/laws">axle.algebra.laws</a> package
as <a href="http://scalacheck.org/">ScalaCheck</a> properties.
They are organized with <a href="https://github.com/typelevel/discipline">Discipline</a>.
</p>
</div>

  val chapter = Chapter("Algebra", intro)

}
