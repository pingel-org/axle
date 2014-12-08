package axle.visualize

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Paint
import java.awt.Stroke
import java.awt.event.MouseEvent

import org.apache.commons.collections15.Transformer

import axle.algebra.Edge
import axle.algebra.UndirectedGraph
import axle.algebra.Vertex
import axle.Show
import axle.string
import axle.jung.JungUndirectedGraph
import axle.jung.JungUndirectedGraphEdge
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position

class JungUndirectedGraphVisualization(width: Int = 700, height: Int = 700, border: Int = 50) {

  def component[VP: Show, EP: Show](jug: JungUndirectedGraph[VP, EP]): Component = {

    val layout = new FRLayout(jug.jusg)
    layout.setSize(new Dimension(width, height))
    val vv = new VisualizationViewer(layout) // interactive
    vv.setPreferredSize(new Dimension(width + border, height + border))
    vv.setMinimumSize(new Dimension(width + border, height + border))

    val vertexPaint = new Transformer[Vertex[VP], Paint]() {
      def transform(i: Vertex[VP]): Paint = Color.GREEN
    }

    val dash = List(10.0f).toArray

    val edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f)

    val edgeStrokeTransformer = new Transformer[JungUndirectedGraphEdge[VP, EP], Stroke]() {
      def transform(edge: JungUndirectedGraphEdge[VP, EP]): BasicStroke = edgeStroke
    }

    val vertexLabelTransformer = new Transformer[Vertex[VP], String]() {
      def transform(vertex: Vertex[VP]): String = string(vertex.payload)
    }

    val edgeLabelTransformer = new Transformer[JungUndirectedGraphEdge[VP, EP], String]() {
      def transform(edge: JungUndirectedGraphEdge[VP, EP]): String = string(edge.payload)
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
