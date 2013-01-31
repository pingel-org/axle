package axle.visualize

trait Behavior[I, +A] {

  def observe(input: I): A

//  def map[B](f: A => B): Behavior[I, B] = ???
//
//  def flatMap[B](f: A => Behavior[I, B]): Behavior[I, B] = ???

}
