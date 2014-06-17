package axle.visualize

object DataFeedProtocol {
  case class RegisterViewer()
  case class Recompute()
  case class Fetch()
}
