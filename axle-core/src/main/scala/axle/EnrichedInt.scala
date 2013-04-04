package axle

case class EnrichedInt(n: Int) {

  def factorial(): Int = (1 to n).foldLeft(1)(_ * _)

}