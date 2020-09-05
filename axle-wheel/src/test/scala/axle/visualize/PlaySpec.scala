package axle.visualize

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import java.net.URL
import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._
import spire.algebra.Field
import spire.random.Generator.rng
import monix.reactive._
import monix.execution.Scheduler.Implicits.global

import axle.reactive.intervalScan
import axle.quanta.Time
import axle.jung.directedGraphJung
import axle.reactive.CurrentValueSubscriber
import axle.awt.play

class PlaySpec extends AnyFunSuite with Matchers {

  test("BarChart play to awt fruit sales") {

    val sales = Map(
      "apple" -> 83.8,
      "banana" -> 77.9,
      "coconut" -> 10.1)

    val update: Map[String, Double] => Map[String, Double] = (old: Map[String, Double]) =>
      Map(
        "apple" -> rng.nextDouble() * 100d,
        "banana" -> rng.nextDouble() * 100d,
        "coconut" -> rng.nextDouble() * 100d)

    implicit val tr = {
      implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
      import axle.algebra.modules._
      Time.converterGraphK2[Double, DirectedSparseGraph]
    }
    import tr._
    val dataUpdates: Observable[Map[String, Double]] = intervalScan(sales, update, 1d *: second)

    val cvSub = new CurrentValueSubscriber[Map[String, Double]]()
    val cvCancellable = dataUpdates.subscribe(cvSub)

    // implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    import spire.algebra.AdditiveMonoid
    implicit val additiveMonoidDouble: AdditiveMonoid[Double] = spire.implicits.DoubleAlgebra
    val chart = BarChart[String, Double, Map[String, Double], String](
      () => cvSub.currentValue.getOrElse(sales),
      title = Some("fruit sales"),
      xAxis = Some(0d),
      labelAngle = Some(36d *: angleDouble.degree),
      hoverOf = (c: String) => Some(c),
      linkOf = (c: String) => Some((new URL(s"http://wikipedia.org/wiki/$c"), Color.lightGray)))

    val (frame, paintCancellable) = play(chart, dataUpdates)

    paintCancellable.cancel()
    frame.setVisible(false)
    cvCancellable.cancel()

    1 should be(1) // TODO
  }
}