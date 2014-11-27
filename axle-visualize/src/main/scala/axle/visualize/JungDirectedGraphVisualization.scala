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
import axle.jung.JungDirectedGraph
import axle.graph.Vertex
import axle.Show
import axle.string
import axle.HtmlFrom
import axle.html
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position

class JungDirectedGraphVisualization(width: Int = 700, height: Int = 700, border: Int = 50) {

  def component[VP: HtmlFrom, EP: Show](jdg: JungDirectedGraph[VP, EP]): Component = {

    // see
    // http://www.grotto-networking.com/JUNG/
    // http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf

    val layout = new FRLayout(jdg.storage)
    layout.setSize(new Dimension(width, height))
    // val vv = new BasicVisualizationServer[ug.type#V, ug.type#E](layout) // non-interactive
    val vv = new VisualizationViewer(layout) // interactive
    vv.setPreferredSize(new Dimension(width + border, height + border))
    vv.setMinimumSize(new Dimension(width + border, height + border))

    val vertexPaint = new Transformer[Vertex[VP], Paint]() {
      def transform(i: Vertex[VP]): Paint = Color.GREEN
    }

    val dash = List(10.0f).toArray

    val edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f)

    val edgeStrokeTransformer = new Transformer[Edge[jdg.ES, EP], Stroke]() {
      def transform(e: Edge[jdg.ES, EP]): BasicStroke = edgeStroke
    }

    val vertexLabelTransformer = new Transformer[Vertex[VP], String]() {
      def transform(v: Vertex[VP]): String = string(html(v.payload))
    }

    val edgeLabelTransformer = new Transformer[Edge[jdg.ES, EP], String]() {
      def transform(e: Edge[jdg.ES, EP]): String = string(e.payload)
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
