package axle.visualize

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Paint
import java.awt.Stroke
import java.awt.event.MouseEvent

import org.apache.commons.collections15.Transformer

import axle.graph.Edge
import axle.graph.JungUndirectedGraph
import axle.graph.Vertex
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position

class JungUndirectedGraphVisualization(width: Int = 700, height: Int = 700, border: Int = 50) {

  def component[VP, EP](jug: JungUndirectedGraph[VP, EP]): Component = {

    val layout = new FRLayout(jug.storage)
    layout.setSize(new Dimension(width, height))
    val vv = new VisualizationViewer(layout) // interactive
    vv.setPreferredSize(new Dimension(width + border, height + border))
    vv.setMinimumSize(new Dimension(width + border, height + border))

    val vertexPaint = new Transformer[Vertex[VP], Paint]() {
      def transform(i: Vertex[VP]): Paint = Color.GREEN
    }

    val dash = List(10.0f).toArray

    val edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f)

    val edgeStrokeTransformer = new Transformer[Edge[jug.ES, EP], Stroke]() {
      def transform(edge: Edge[jug.ES, EP]): BasicStroke = edgeStroke
    }

    val vertexLabelTransformer = new Transformer[Vertex[VP], String]() {
      def transform(vertex: Vertex[VP]): String = vertex.payload.toString
    }

    val edgeLabelTransformer = new Transformer[Edge[jug.ES, EP], String]() {
      def transform(edge: Edge[jug.ES, EP]): String = edge.payload.toString
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
