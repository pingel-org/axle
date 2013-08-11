
/*
 * These are my notes from Nick Partidge's presentation to the Melbourne Scala Users Group
 * on "Deriving Scalaz"
 *
 * The original video is available at http://vimeo.com/10482466
 *
 * I've separated each of the steps in the derivation into separate StepX objects
 *
 * The steps I've captured are all covered in the first 40 minutes of the video.
 * They cover the derivation of Monoid, FoldLeft, Identity, and MA "type classes".
 * This is the tehnique that Scalaz uses everywhere for ad-hoc polymorphism.
 *
 * Scalaz also provides Functor, Monad (Pure + Bind), and many other type classes.
 * Monoid is actually Semigroup + Zero
 *
 */

/*
  * We start with a "sum" function defined like so:
  *
  *   def sum(xs: List[Int]): Int
  *
  * With the goal of generalizing to sum higher kinded *anything*:
  *
  *   def sum[M[_], A](xs: M[A])(...): A
  *
  */

object NickPartridgeScalaz1 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  def sum(xs: List[Int]): Int = xs.foldLeft(0) { _ + _ }
                                                  //> sum: (xs: List[Int])Int

  sum(List(1, 2, 3, 4))                           //> res0: Int = 10
}