package axle.visualize

import java.awt.event.MouseEvent
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.Paint
import java.awt.Stroke

import org.apache.commons.collections15.Transformer

import axle.graph.JungDirectedGraphFactory.{JungDirectedGraph => jdg}
import axle.graph.JungDirectedGraphFactory.JungDirectedGraph
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position
import edu.uci.ics.jung.visualization.VisualizationViewer

class JungDirectedGraphVisualization() {

  def component[VP, EP](jdg: JungDirectedGraph[VP, EP]) = {

    type V = jdg.type#V
    type E = jdg.type#E

    val width = 700
    val height = 700
    val border = 50

    // see
    // http://www.grotto-networking.com/JUNG/
    // http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf

    val layout = new FRLayout(jdg.getStorage)
    layout.setSize(new Dimension(width, height))
    // val vv = new BasicVisualizationServer[ug.type#V, ug.type#E](layout) // non-interactive
    val vv = new VisualizationViewer[V, E](layout) // interactive
    vv.setPreferredSize(new Dimension(width + border, height + border))

    val vertexPaint = new Transformer[V, Paint]() {
      def transform(i: V): Paint = Color.GREEN
    }

    val dash = List(10.0f).toArray

    val edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f)

    val edgeStrokeTransformer = new Transformer[E, Stroke]() {
      def transform(edge: E) = edgeStroke
    }

    val vertexLabelTransformer = new Transformer[V, String]() {
      def transform(vertex: V) = vertex.getPayload.toString
    }

    val edgeLabelTransformer = new Transformer[E, String]() {
      def transform(edge: E) = edge.getPayload.toString
    }

    vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint)
    vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer)
    vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer) // new ToStringLabeller())
    vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer)
    vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR)

    // val gm = new DefaultModalGraphMouse()
    // gm.setMode(ModalGraphMouse.Mode.TRANSFORMING)
    val gm = new PluggableGraphMouse()
    gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1))
    gm.add(new PickingGraphMousePlugin())
    // gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f))
    vv.setGraphMouse(gm)

    // val frame = new JFrame("graph name")
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    // frame.getContentPane().add(vv)
    // frame.pack()
    // frame.setVisible(true)
    vv
  }

}