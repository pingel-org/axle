package axle.visualize.gl

case class Color(red: Float, green: Float, blue: Float)

object Color {
  val lightBlue = Color(0.5f, 0.5f, 1.0f)
  val black = Color(0.0f, 0.0f, 0.0f)
  val white = Color(1.0f, 1.0f, 1.0f)
  val gray = Color(0.6f, 0.6f, 0.6f)
  val red = Color(1.0f, 0.0f, 0.0f)
  val green = Color(0.0f, 1.0f, 0.0f)
  val blue = Color(0.0f, 0.0f, 0.1f)
  val yellow = Color(1.0f, 1.0f, 0.0f)
  val lightYellow = Color(.5f, .5f, 0.0f)
}
