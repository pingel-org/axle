package org.pingel.bayes;

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Paint
import java.awt.Stroke

import javax.swing.JFrame

import edu.uci.ics.jung.graph.DirectedGraph
import edu.uci.ics.jung.graph.Edge
import edu.uci.ics.jung.graph.Vertex
import edu.uci.ics.jung.graph.decorators.EdgeShape
import edu.uci.ics.jung.graph.decorators.EdgeStrokeFunction
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction
import edu.uci.ics.jung.graph.decorators.VertexStringer
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex
import edu.uci.ics.jung.visualization.FRLayout
import edu.uci.ics.jung.visualization.GraphDraw
import edu.uci.ics.jung.visualization.Layout
import edu.uci.ics.jung.visualization.PluggableRenderer

class VisVariableVertex(variable: RandomVariable) extends DirectedSparseVertex {

}

class VisModelEdge(source: VisVariableVertex, dest: VisVariableVertex) extends DirectedSparseEdge {

}

class ModelVertexPaintFunction(m: Model) extends VertexPaintFunction {
	
  def getDrawPaint(v: Vertex): Paint = Color.BLACK
    
  def getFillPaint(v: Vertex): Paint = {
    if( v.asInstanceOf[VisVariableVertex].variable.observable ) {
      return Color.BLUE
    }
    else {
      return Color.WHITE
    }
  }
}

object Strokes {

  val basic = new BasicStroke(1)
  val dotted = PluggableRenderer.DOTTED

}

class ModelEdgeStrokeFunction(m: Model) extends EdgeStrokeFunction {

  def getStroke(e: Edge): Stroke = {
    var me = e.asInstanceOf[VisModelEdge]
    if( me.source.variable.observable ) {
      return Strokes.basic
    }
    else {
      return Strokes.dotted
    }
  }
    
}

class ModelVertexStringer(m: Model) extends VertexStringer {
   
  def getLabel(v: Vertex): String = v.asInstanceOf[VisVariableVertex].variable.name

}

object ModelVisualizer {

  def modelToGraph(m: Model, graph: DirectedGraph): Unit = {
    var var2vvv = Map[RandomVariable, VisVariableVertex]() 
    for( variable <- m.getGraph.getVertices ) {
      var vvv = new VisVariableVertex(variable)
      graph.addVertex(vvv)
      var2vvv.put(variable, vvv)
    }
    for( edge <- m.getGraph.getEdges ) {
      var sourceVar = edge.getSource()
      var destVar = edge.getDest()
      var vme = new VisModelEdge(var2vvv.get(sourceVar), var2vvv.get(destVar))
      graph.addEdge(vme)
    }
  }
    
  def draw(m: Model): Unit = {

    var pr = new PluggableRenderer()
    pr.setVertexPaintFunction(new ModelVertexPaintFunction(m))
    pr.setEdgeStrokeFunction(new ModelEdgeStrokeFunction(m))
    pr.setEdgeShapeFunction(new EdgeShape.Line())
    pr.setVertexStringer(new ModelVertexStringer(m))

    var graph = new DirectedSparseGraph()

    modelToGraph(m, graph)
        
    var layout = new FRLayout(graph)
    
    var jf = new JFrame()
    var gd = new GraphDraw(graph)
    gd.getVisualizationViewer().setGraphLayout(layout)
    gd.getVisualizationViewer().setRenderer(pr)
    jf.getContentPane().add(gd)
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    jf.pack()
    jf.setVisible(true)
  }
}
