package axle.pgm

import scala.xml.NodeSeq.seqToNodeSeq

import axle.HtmlFrom
import cats.Show
import axle.stats.Distribution
import axle.stats.Factor
import axle.string
import cats.kernel.Eq
import spire.algebra.Field

case class BayesianNetworkNode[T, N](rv: Distribution[T, N], cpt: Factor[T, N])

object BayesianNetworkNode {

  implicit def bnnShow[T, N]: Show[BayesianNetworkNode[T, N]] =
    new Show[BayesianNetworkNode[T, N]] {

      def show(bnn: BayesianNetworkNode[T, N]): String = {
        import bnn._
        rv.name + "\n\n" + cpt
      }

    }

  implicit def bnnHtmlFrom[T: Show, N]: HtmlFrom[BayesianNetworkNode[T, N]] =
    new HtmlFrom[BayesianNetworkNode[T, N]] {
      def toHtml(bnn: BayesianNetworkNode[T, N]): xml.Node =
        <div>
          <h2>{ bnn.rv.name }</h2>
          <table border={ "1" }>
            <tr>{ bnn.cpt.varList.map(rv => <td>{ rv.name }</td>): xml.NodeSeq }<td>P</td></tr>
            {
              bnn.cpt.cases.map(kase =>
                <tr>
                  { kase.map(ci => <td>{ string(ci.v) }</td>) }
                  <td>{ bnn.cpt(kase) }</td>
                </tr>)
            }
          </table>
        </div>
    }

  implicit def bnnEq[T: Eq, N: Field] = new Eq[BayesianNetworkNode[T, N]] {
    def eqv(x: BayesianNetworkNode[T, N], y: BayesianNetworkNode[T, N]): Boolean =
      x equals y
  }
}
