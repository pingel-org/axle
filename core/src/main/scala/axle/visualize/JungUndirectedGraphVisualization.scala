package axle.visualize

import java.awt.event.MouseEvent
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.Paint
import java.awt.Stroke
import java.awt.Component

import org.apache.commons.collections15.Transformer

import axle.graph.JungUndirectedGraph._
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position
import edu.uci.ics.jung.visualization.VisualizationViewer

class JungUndirectedGraphVisualization(width: Int = 700, height: Int = 700, border: Int = 50) {

  def component[VP, EP](jug: JungUndirectedGraph[VP, EP]): Component = {

    // type V = jug.type#V
    // type E = jug.type#E

    val layout = new FRLayout(jug.storage)
    layout.setSize(new Dimension(width, height))
    // JungUndirectedGraphVertex[VP], JungUndirectedGraphEdge[VP, EP]
    val vv = new VisualizationViewer(layout) // interactive
    vv.setPreferredSize(new Dimension(width + border, height + border))

    val vertexPaint = new Transformer[JungUndirectedGraphVertex[VP], Paint]() {
      def transform(i: JungUndirectedGraphVertex[VP]): Paint = Color.GREEN
    }

    val dash = List(10.0f).toArray

    val edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f)

    val edgeStrokeTransformer = new Transformer[JungUndirectedGraphEdge[VP, EP], Stroke]() {
      def transform(edge: JungUndirectedGraphEdge[VP, EP]) = edgeStroke
    }

    val vertexLabelTransformer = new Transformer[JungUndirectedGraphVertex[VP], String]() {
      def transform(vertex: JungUndirectedGraphVertex[VP]) = vertex.payload.toString
    }

    val edgeLabelTransformer = new Transformer[JungUndirectedGraphEdge[VP, EP], String]() {
      def transform(edge: JungUndirectedGraphEdge[VP, EP]) = edge.payload.toString
    }

    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint)
    vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer)
    vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer)
    vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer)
    vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR)

    val gm = new PluggableGraphMouse()
    gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1))
    gm.add(new PickingGraphMousePlugin())
    vv.setGraphMouse(gm)

    vv
  }

}
