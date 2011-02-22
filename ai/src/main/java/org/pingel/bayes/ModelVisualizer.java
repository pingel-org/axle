package org.pingel.bayes;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EdgeStrokeFunction;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.GraphDraw;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderer;


class VisVariableVertex extends DirectedSparseVertex
{
    public RandomVariable var;
    
    VisVariableVertex(RandomVariable var)
    {
        this.var = var;
    }
}

class VisModelEdge extends DirectedSparseEdge
{
    public VisVariableVertex source;
    public VisVariableVertex dest;
    
    VisModelEdge(VisVariableVertex source, VisVariableVertex dest)
    {
        super(source, dest);
        this.source = source;
        this.dest = dest;
    }
}

class ModelVertexPaintFunction implements VertexPaintFunction
{
	Model model;
	
	ModelVertexPaintFunction(Model m)
	{
		this.model = m;
	}
	
    public Paint getDrawPaint(Vertex v)
    {
        return Color.BLACK;
    }
    
    public Paint getFillPaint(Vertex v)
    {
        VisVariableVertex vv = (VisVariableVertex) v;
        if( vv.var.observable ) {
            return Color.BLUE;
        }
        else {
            return Color.WHITE;
        }
    }
}


class ModelEdgeStrokeFunction implements EdgeStrokeFunction
{
    protected static final Stroke basic = new BasicStroke(1);
    protected static final Stroke dotted = PluggableRenderer.DOTTED;

    Model model;
    
    ModelEdgeStrokeFunction(Model m)
    {
        this.model = m;
    }

    public Stroke getStroke(Edge e)
    {
        VisModelEdge me = (VisModelEdge) e;
        
        if( me.source.var.observable ) {
            return basic;
        }
        else {
            return dotted;
        }
    }
    
}

class ModelVertexStringer implements VertexStringer
{

    Model model;
    
    ModelVertexStringer(Model m)
    {
        this.model = m;
    }
    
    public String getLabel(Vertex v)
    {
        VisVariableVertex vv = (VisVariableVertex) v;
        return vv.var.name;
    }

}

public class ModelVisualizer {


    private static void modelToGraph(Model m, DirectedGraph graph)
    {
        Map<RandomVariable, VisVariableVertex> var2vvv = new HashMap<RandomVariable, VisVariableVertex>(); 
        for( RandomVariable var : m.getGraph().getVertices() ) {
            VisVariableVertex vvv = new VisVariableVertex(var);
            graph.addVertex(vvv);
            var2vvv.put(var, vvv);
        }
        for( ModelEdge edge : m.getGraph().getEdges() ) {
            RandomVariable sourceVar = edge.getSource();
            RandomVariable destVar = edge.getDest();
            VisModelEdge vme = new VisModelEdge(var2vvv.get(sourceVar), var2vvv.get(destVar));
            graph.addEdge(vme);
        }
    }
    
	public static void draw(Model m)
	{

        PluggableRenderer pr = new PluggableRenderer();
        pr.setVertexPaintFunction(new ModelVertexPaintFunction(m));
        pr.setEdgeStrokeFunction(new ModelEdgeStrokeFunction(m));
        pr.setEdgeShapeFunction(new EdgeShape.Line());
        pr.setVertexStringer(new ModelVertexStringer(m));

        DirectedGraph graph = new DirectedSparseGraph();

        modelToGraph(m, graph);
        
        Layout layout = new FRLayout(graph);
        
	    JFrame jf = new JFrame();
		GraphDraw gd = new GraphDraw(graph);
        gd.getVisualizationViewer().setGraphLayout(layout);
		gd.getVisualizationViewer().setRenderer(pr);
		jf.getContentPane().add(gd);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
}
