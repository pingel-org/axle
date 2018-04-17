package axle.pgm

import cats.Show
import cats.kernel.Eq
import cats.implicits._

import spire.algebra.Field

import axle.stats.Variable
import axle.stats.Factor
import axle.HtmlFrom

case class BayesianNetworkNode[T, N](variable: Variable[T], cpt: Factor[T, N])

object BayesianNetworkNode {

  implicit def bnnShow[T, N]: Show[BayesianNetworkNode[T, N]] = bnn =>
    bnn.variable.name + "\n\n" + bnn.cpt

  implicit def bnnHtmlFrom[T: Show, N]: HtmlFrom[BayesianNetworkNode[T, N]] =
    new HtmlFrom[BayesianNetworkNode[T, N]] {
      def toHtml(bnn: BayesianNetworkNode[T, N]): xml.Node =
        <div>
          <h2>{ bnn.variable.name }</h2>
          <table border={ "1" }>
            <tr>{ bnn.cpt.variables.map(variable => <td>{ variable.name }</td>): xml.NodeSeq }<td>P</td></tr>
            {
              bnn.cpt.cases.map(kase =>
                <tr>
                  { kase.map(ci => <td>{ ci.value.show }</td>) }
                  <td>{ bnn.cpt(kase) }</td>
                </tr>)
            }
          </table>
        </div>
    }

  implicit def bnnEq[T: Eq, N: Field]: Eq[BayesianNetworkNode[T, N]] =
    (x, y) => x equals y

}
