package axle.awt

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Paint
import java.awt.Stroke
import java.awt.event.MouseEvent

import org.apache.commons.collections15.Transformer

import axle.HtmlFrom
import axle.Show
import axle.html
import axle.string
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position
import edu.uci.ics.jung.graph.DirectedSparseGraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph

case class JungDirectedGraphVisualization(width: Int = 700, height: Int = 700, border: Int = 50) {

  def component[VP: HtmlFrom, EP: Show](jdsg: DirectedSparseGraph[VP, EP]): Component = {

    // see
    // http://www.grotto-networking.com/JUNG/
    // http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf

    val layout = new FRLayout(jdsg)
    layout.setSize(new Dimension(width, height))
    // val vv = new BasicVisualizationServer[ug.type#V, ug.type#E](layout) // non-interactive
    val vv = new VisualizationViewer(layout) // interactive
    vv.setPreferredSize(new Dimension(width + border, height + border))
    vv.setMinimumSize(new Dimension(width + border, height + border))

    val vertexPaint = new Transformer[VP, Paint]() {
      def transform(i: VP): Paint = Color.GREEN
    }

    val dash = List(10f).toArray

    val edgeStroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f)

    val edgeStrokeTransformer = new Transformer[EP, Stroke]() {
      def transform(e: EP): BasicStroke = edgeStroke
    }

    val vertexLabelTransformer = new Transformer[VP, String]() {
      def transform(v: VP): String = {
        val label = html(v)
        label match {
          case xml.Text(text) => text
          case _              => string((<html>{ label }</html>).asInstanceOf[xml.Node])
        }
      }
    }

    val edgeLabelTransformer = new Transformer[EP, String]() {
      def transform(e: EP): String = string(e)
    }

    vv.getRenderContext.setVertexFillPaintTransformer(vertexPaint)
    vv.getRenderContext.setEdgeStrokeTransformer(edgeStrokeTransformer)
    vv.getRenderContext.setVertexLabelTransformer(vertexLabelTransformer)
    vv.getRenderContext.setEdgeLabelTransformer(edgeLabelTransformer)
    vv.getRenderer.getVertexLabelRenderer.setPosition(Position.CNTR)

    // val gm = new DefaultModalGraphMouse()
    // gm.setMode(ModalGraphMouse.Mode.TRANSFORMING)
    val gm = new PluggableGraphMouse()
    gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1))
    gm.add(new PickingGraphMousePlugin())
    // gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f))
    vv.setGraphMouse(gm)
    vv
  }

}
