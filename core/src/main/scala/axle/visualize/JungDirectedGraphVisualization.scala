package axle.visualize

import axle.graph.JungDirectedGraphFactory._

import javax.swing.JFrame
import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke }
import java.awt.event.MouseEvent

import edu.uci.ics.jung.algorithms.layout.{ Layout, FRLayout, CircleLayout }
import edu.uci.ics.jung.graph.{ Graph, SparseGraph, SparseMultigraph }
import edu.uci.ics.jung.visualization.{ BasicVisualizationServer, VisualizationViewer }
import edu.uci.ics.jung.visualization.control.{ CrossoverScalingControl, DefaultModalGraphMouse, PluggableGraphMouse, PickingGraphMousePlugin, TranslatingGraphMousePlugin, ModalGraphMouse, ScalingGraphMousePlugin }
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position

import org.apache.commons.collections15.Transformer

class JungDirectedGraphVisualization() {

  def draw[VP, EP](jdg: JungDirectedGraph[VP, EP]): Unit = {

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

    val frame = new JFrame("graph name")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.getContentPane().add(vv)
    frame.pack()
    frame.setVisible(true)
  }

}