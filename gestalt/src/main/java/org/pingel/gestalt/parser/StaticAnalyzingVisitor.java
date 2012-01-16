
package org.pingel.gestalt.parser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.pingel.gestalt.core.GLogger;
import org.pingel.gestalt.core.Lambda;
import org.pingel.gestalt.core.Name;
import org.pingel.gestalt.core.TransformEdge;
import org.pingel.gestalt.parser.syntaxtree.Application;
import org.pingel.gestalt.parser.syntaxtree.Expression;
import org.pingel.gestalt.parser.syntaxtree.Form;
import org.pingel.gestalt.parser.syntaxtree.GSystem;
import org.pingel.gestalt.parser.syntaxtree.Goal;
import org.pingel.gestalt.parser.syntaxtree.Identifier;
import org.pingel.gestalt.parser.syntaxtree.Include;
import org.pingel.gestalt.parser.syntaxtree.Node;
import org.pingel.gestalt.parser.syntaxtree.NodeSequence;
import org.pingel.gestalt.parser.syntaxtree.Path;
import org.pingel.gestalt.parser.syntaxtree.Statement;
import org.pingel.gestalt.parser.syntaxtree.Substitution;
import org.pingel.gestalt.parser.syntaxtree.Transform;
import org.pingel.gestalt.parser.visitor.ObjectDepthFirst;

public class StaticAnalyzingVisitor extends ObjectDepthFirst {

    public org.pingel.gestalt.core.Lexicon lexicon;

    private List<String> includes = new Vector<String>();

    public List<String> getIncludes()
    {
	return includes;
    }

    public StaticAnalyzingVisitor(org.pingel.gestalt.core.Lexicon l)
    {
	this.lexicon = l;
    }

    /**
     * f0 -> ( Statement() )*
     * f1 -> <EOF>
     */
    public Object visit(Goal n, Object argu) { // done


	n.f0.accept(this, null);

	return null;
    }

    /**
     * f0 -> Transform()
     *       | GSystem()
     *       | Form()
     *       | Include()
     */
    public Object visit(Statement n, Object argu) { // done

	n.f0.accept(this, null);

	return null;
    }

    /**
     * f0 -> "include"
     * f1 -> Identifier()
     */
    public Object visit(Include n, Object argu) {
	
	includes.add(n.f1.f0.toString());

	return null;
    }

    /**
     * f0 -> "rule"
     * f1 -> Identifier()
     * f2 -> Identifier()
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( Substitution() )*
     * f6 -> "}"
     * f7 -> ( "$" Identifier() )?
     */
    public Object visit(Transform n, Object argu) {

	org.pingel.gestalt.core.Name ruleName;
	org.pingel.gestalt.core.Name inName;
	org.pingel.gestalt.core.Name outName;

	ruleName = new org.pingel.gestalt.core.Name(n.f1.f0.toString());
	inName = new org.pingel.gestalt.core.Name(n.f2.f0.toString());
	outName = new org.pingel.gestalt.core.Name(n.f3.f0.toString());

	Map<org.pingel.gestalt.core.Name, org.pingel.gestalt.core.Name> map =
        new TreeMap<org.pingel.gestalt.core.Name, org.pingel.gestalt.core.Name>();
	n.f5.accept(this, map);

	double cost = 0;
	if( n.f7.present() ) {
	    Node costIdNode = ((NodeSequence)(n.f7.node)).elementAt(1);
	    cost = Double.parseDouble((((Identifier)costIdNode).f0).toString());
	}
	
	org.pingel.gestalt.core.SimpleTransform r =
	    new org.pingel.gestalt.core.SimpleTransform(inName, outName, map, cost);
	
	lexicon.put(ruleName, r);

	return null;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "/"
     * f2 -> Identifier()
     */
    public Object visit(Substitution n, Object argu) {

	Map<org.pingel.gestalt.core.Name, org.pingel.gestalt.core.Name> map = (Map<org.pingel.gestalt.core.Name, org.pingel.gestalt.core.Name>) argu;

	org.pingel.gestalt.core.Name from = new org.pingel.gestalt.core.Name(n.f0.f0.toString());
	org.pingel.gestalt.core.Name to = new org.pingel.gestalt.core.Name(n.f2.f0.toString());

	GLogger.global.fine("adding " + from + "/" + to + " to substitution map");
	
	map.put(from, to);

	return null;
    }

    /**
     * f0 -> "system"
     * f1 -> Identifier()
     * f2 -> Identifier()
     * f3 -> "{"
     * f4 -> ( Application() )*
     * f5 -> "}"
     * f6 -> "<"
     * f7 -> ( Identifier() )*
     * f8 -> ">"
     */
    public Object visit(GSystem n, Object argu) {
	
	org.pingel.gestalt.core.Name name = new org.pingel.gestalt.core.Name(n.f1.f0.toString());

	org.pingel.gestalt.core.Name guardName = new org.pingel.gestalt.core.Name(n.f2.f0.toString());

	org.pingel.gestalt.core.ComplexTransform t =
	    new org.pingel.gestalt.core.ComplexTransform(guardName);

//	StringLabeller labeller = GlobalStringLabeller.getLabeller(t);

	Map<org.pingel.gestalt.core.Name, org.pingel.gestalt.core.TransformVertex> name2node=
        new TreeMap<org.pingel.gestalt.core.Name, org.pingel.gestalt.core.TransformVertex>();

	Object[] a = {t, name2node};
	n.f4.accept(this, a);

	org.pingel.gestalt.core.TransformVertex startNode = name2node.get(new org.pingel.gestalt.core.Name("in"));
	startNode.isStart(true);
	t.start = startNode;

	if( startNode == null ) {
	    GLogger.global.severe("no input defined");
	    System.exit(1);
	}

	Set<org.pingel.gestalt.core.Name> exitNames = new HashSet<org.pingel.gestalt.core.Name>();
	n.f7.accept(this, exitNames);

	Set<org.pingel.gestalt.core.TransformVertex> exitNodes = new HashSet<org.pingel.gestalt.core.TransformVertex>();

	Iterator<org.pingel.gestalt.core.Name> exit_it = exitNames.iterator();
	while( exit_it.hasNext() ) {

	    org.pingel.gestalt.core.Name eName = exit_it.next();
	    org.pingel.gestalt.core.TransformVertex eNode = name2node.get(eName);
	    eNode.isExit(true);

	    exitNodes.add(eNode);
	    GLogger.global.info("marking " + eName.toString() + " as exit");

	}

	GLogger.global.info("registering transform " + name);
	
	lexicon.put(name, t);

	GLogger.global.info("details:\n" + t.toString());
	
	return null;
    }

    /**
     * f0 -> "apply"
     * f1 -> Identifier()
     * f2 -> Identifier()
     * f3 -> ( Path() )?
     * f4 -> Identifier()
     */
    public Object visit(Application n, Object argu) {
        
        Object[] arguList = (Object[]) argu;
        
        org.pingel.gestalt.core.ComplexTransform t = (org.pingel.gestalt.core.ComplexTransform) arguList[0];
        Map<org.pingel.gestalt.core.Name, org.pingel.gestalt.core.TransformVertex> name2node =
            (Map<org.pingel.gestalt.core.Name, org.pingel.gestalt.core.TransformVertex>) arguList[1];
        
        org.pingel.gestalt.core.Name transformName = new org.pingel.gestalt.core.Name(n.f1.f0.toString());
        org.pingel.gestalt.core.Name inNodeName = new org.pingel.gestalt.core.Name(n.f2.f0.toString());
        
        org.pingel.gestalt.core.TransformVertex inNode = name2node.get(inNodeName);
        if( inNode == null ) {
            inNode = new org.pingel.gestalt.core.TransformVertex(inNodeName, false, false);
            t.getGraph().addVertex(inNode);
            name2node.put(inNodeName, inNode);
        }

        org.pingel.gestalt.core.Traversal traversal = null;
        if( n.f3.present() ) {
            traversal = (org.pingel.gestalt.core.Traversal) n.f3.accept(this, null);
        }
        
        org.pingel.gestalt.core.Name outNodeName = new org.pingel.gestalt.core.Name(n.f4.f0.toString());
        
        org.pingel.gestalt.core.TransformVertex outNode = name2node.get(outNodeName);
        if( outNode == null ) {
            outNode = new org.pingel.gestalt.core.TransformVertex(outNodeName, false, false);
            t.getGraph().addVertex(outNode);
            name2node.put(outNodeName, outNode);
        }

        TransformEdge arc = new org.pingel.gestalt.core.TransformEdge(transformName, traversal, inNode, outNode);
        t.getGraph().addEdge(arc);
        
        return null;
    }

    /**
     * f0 -> "firm"
     * f1 -> Identifier()
     * f2 -> "["
     * f3 -> ( Identifier() )*
     * f4 -> "]"
     * f5 -> Expression()
     */
    public Object visit(Form n, Object argu) { // done

		org.pingel.gestalt.core.Name name = new org.pingel.gestalt.core.Name(n.f1.f0.toString());

		TreeSet scopedVars = new TreeSet();
		n.f3.accept(this, scopedVars);

		GLogger.global.info("scoped vars for form " + name + " are " + scopedVars);
		
		org.pingel.gestalt.core.Form f = (org.pingel.gestalt.core.Form) n.f5.accept(this, scopedVars);
      
		lexicon.put(name, f);

		return null;
    }

    /**
     * f0 -> Identifier()
     *       | "(" Expression() Expression() ")"
     */
    public Object visit(Expression n, Object argu) { // done

		TreeSet<org.pingel.gestalt.core.Name> scopedVars = (TreeSet<org.pingel.gestalt.core.Name>) argu;

        org.pingel.gestalt.core.Name top = new Name("top");
        
        Lambda lambda = new Lambda();
        if( scopedVars != null ) {
            for( org.pingel.gestalt.core.Name name : scopedVars ) {
                lambda.add(name, top);
            }
        }
        
		if( n.f0.which == 0 ) {

		    org.pingel.gestalt.core.Name aname = new org.pingel.gestalt.core.Name(((Identifier)(n.f0.choice)).f0.toString());
            
		    return new org.pingel.gestalt.core.SimpleForm(aname, lambda);

		}
		else {
	    
		    org.pingel.gestalt.core.Form s1 = 
				(org.pingel.gestalt.core.Form)((NodeSequence)(n.f0.choice)).elementAt(1).accept(this, null);
		    org.pingel.gestalt.core.Form s2 = 
				(org.pingel.gestalt.core.Form)((NodeSequence)(n.f0.choice)).elementAt(2).accept(this, null);
            
		    return new org.pingel.gestalt.core.ComplexForm(s1, s2, lambda);
		}

    }

    /**
     * f0 -> <PATH>
     */
    public Object visit(Path n, Object argu) { // done
	
	String pathString = n.f0.toString().substring(1);

	return new org.pingel.gestalt.core.Traversal(pathString);

    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public Object visit(Identifier n, Object argu) { // done

	if( argu != null) {
	    Set<org.pingel.gestalt.core.Name> s = (Set<org.pingel.gestalt.core.Name>) argu;
	    s.add(new org.pingel.gestalt.core.Name(n.f0.toString()));
	}

	return null;
    }

}
