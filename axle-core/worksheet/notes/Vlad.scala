
package org.pingel.category

// http://prezi.com/3yecwrcccpj-/monads-functors-functions-javascala/

/*
trait Monad[T[_]] {
  def unit [X](x: X): T[X]
  def lift [X, Y](f: X => T[Y]): (T[X] => T[Y])
}

class Option[X] extends Monad[Option] {

  def unit [X](x: X): Some(x)

  def lift [X, Y](f: X => Option[Y]) = { 
    (xOpt: Option[X]) => xOpt match {
      case Some(x) => f(x)
      case None    => None
    }
  }

}

class List[X] extends Monad[List] {

  def unit [X](x: X) = List(x)

  def lift_terse [X, Y](f: X => List[Y]) = { xs: List[X] => xs.map( f ).flatten }

  def lift [X, Y](f: X => List[Y]) = { (xs: List[X]) => for (x <- xs; y <- f(x)) yield y }

}
*/
