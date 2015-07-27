package axle.algebra

trait Talliable[F, T, N] {

  def tally(ts: F): Map[T, N]
}
