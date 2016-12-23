package axle.visualize

case class Color(r: Int, g: Int, b: Int)

object Color {

  def toARGB(color: Color): Int = {
    import color._
    (0xff << 24) | (r << 16) | (g << 8) | b
  }

  def toRGB(color: Color): Int = {
    import color._
    (r << 16) | (g << 8) | b
  }

  val white = Color(r = 255, g = 255, b = 255)
  val lightGray = Color(r = 192, g = 192, b = 192)
  val darkGray = Color(r = 64, g = 64, b = 64)
  val black = Color(r = 0, g = 0, b = 0)
  val blue = Color(r = 0, g = 0, b = 255)
  val red = Color(r = 255, g = 0, b = 0)
  val green = Color(r = 0, g = 255, b = 0)
  val orange = Color(r = 255, g = 200, b = 0)
  val pink = Color(r = 255, g = 175, b = 175)
  val yellow = Color(r = 255, g = 255, b = 0)

}
