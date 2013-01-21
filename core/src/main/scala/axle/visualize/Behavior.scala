package axle.visualize

trait Behavior[I, +T] {
  
  def observe(input: I): T
  
}
