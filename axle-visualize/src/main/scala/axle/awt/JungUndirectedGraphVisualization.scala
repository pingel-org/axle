package axle.awt

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Paint
import java.awt.Stroke
import java.awt.event.MouseEvent

import com.google.common.base.{Function => GoogleFunction}

import axle.Show
import axle.string
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position
import edu.uci.ics.jung.graph.UndirectedSparseGraph

case class JungUndirectedGraphVisualization(width: Int = 700, height: Int = 700, border: Int = 50) {

  def component[VP: Show, EP: Show](jusg: UndirectedSparseGraph[VP, EP]): Component = {

    val layout = new FRLayout(jusg)
    layout.setSize(new Dimension(width, height))
    val vv = new VisualizationViewer(layout) // interactive
    vv.setPreferredSize(new Dimension(width + border, height + border))
    vv.setMinimumSize(new Dimension(width + border, height + border))

    val vertexPaint = new GoogleFunction[VP, Paint]() {
      def apply(i: VP): Paint = Color.GREEN
    }

    val dash = List(10f).toArray

    val edgeStroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f)

    val edgeStrokeTransformer = new GoogleFunction[EP, Stroke]() {
      def apply(edge: EP): BasicStroke = edgeStroke
    }

    val vertexLabelTransformer = new GoogleFunction[VP, String]() {
      def apply(vertex: VP): String = string(vertex)
    }

    val edgeLabelTransformer = new GoogleFunction[EP, String]() {
      def apply(edge: EP): String = string(edge)
    }

    vv.getRenderContext.setVertexFillPaintTransformer(vertexPaint)
    vv.getRenderContext.setEdgeStrokeTransformer(edgeStrokeTransformer)
    vv.getRenderContext.setVertexLabelTransformer(vertexLabelTransformer)
    vv.getRenderContext.setEdgeLabelTransformer(edgeLabelTransformer)
    vv.getRenderer.getVertexLabelRenderer.setPosition(Position.CNTR)

    val gm = new PluggableGraphMouse()
    gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1))
    gm.add(new PickingGraphMousePlugin())
    vv.setGraphMouse(gm)

    vv
  }

}
