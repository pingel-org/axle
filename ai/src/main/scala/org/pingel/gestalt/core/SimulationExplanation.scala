package org.pingel.gestalt.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class SimulationExplanation implements Comparable<SimulationExplanation>
{
    private Map<SimpleTransform, ComplexTransform> betaSystemByConstraint = new HashMap<SimpleTransform, ComplexTransform>();
    private Map<SimpleTransform, ComplexTransformCall> betaCallByConstraint = new HashMap<SimpleTransform, ComplexTransformCall>();
    private Map<SimpleTransform, Form> sourceByConstraint = new HashMap<SimpleTransform, Form>();
    private Map<SimpleTransform, Form> destinationByConstraint = new HashMap<SimpleTransform, Form>();
    private Simulation simulation;

    private int steps = 0;

    public Name goal;
    public Form candidate;

    public SimulationExplanation(Simulation simulation, Name goal, Form candidate,
				 Lexicon lexicon)
    {
        this.simulation = simulation;
        this.goal = goal;
        this.candidate = candidate;

        int constraintIndex = 0;
        
        while( constraintIndex < simulation.constraints.size() ) {
            
            SimpleTransform constraint = simulation.constraints.get(constraintIndex);
            
            ComplexTransformCall betaCall = makeBetaCall(constraint, lexicon);
            
            betaCallByConstraint.put(constraint, betaCall);
            
            constraintIndex++;
        }

    }

    private ComplexTransformCall makeBetaCall(SimpleTransform constraint, Lexicon lexicon)
    {
        Map<Name, Form> goal_replacement_map = new TreeMap<Name, Form>();
        goal_replacement_map.put(goal, candidate);

        Map<Name, Form> source_replacement_map = new TreeMap<Name, Form>(); // though I'm only using SimpleFormm
        for( Name from_name : constraint.map.keySet() ) {
            Name to_name = constraint.map.get(from_name);
            // is this replacement free or not ???
            SimpleForm replacement = new SimpleForm(to_name, null);
            source_replacement_map.put(from_name, replacement);
        }
        
        Form source = lexicon.getForm(constraint.guardName).duplicateAndReplace(source_replacement_map);
        source = source.duplicateAndReplace(goal_replacement_map);

        sourceByConstraint.put(constraint, source);

        Form destination = lexicon.getForm(constraint.outName).duplicateAndReplace(goal_replacement_map);

        destinationByConstraint.put(constraint, destination);
            
        GLogger.global.info("SimulationExplanation.makeBetaCall goal is " + goal);
        GLogger.global.info("SimulationExplanation.makeBetaCall source is " + source);
        GLogger.global.info("SimulationExplanation.makeBetaCall destination is " + destination);

        Vector<TransformEdge> arcs = new Vector<TransformEdge>();
        Set<TransformVertex> exits = new HashSet<TransformVertex>();
        
        TransformVertex beta_in_node = new TransformVertex(new Name("in"), true, false);
        exits.add(beta_in_node);
		int procedure_index = 0;
		while( procedure_index < simulation.transforms.size() ) {
            Transform procedure = simulation.transforms.get(procedure_index);
	    	// note: this should be a "wild" Traversal
            // TODO !!!!!!!! arcs.add(new TransformEdge(procedure, beta_in_node, null, beta_in_node));
            procedure_index++;
		}
        
        // TODO does this really need to be the frontier's createAtom() ?
	
		if( simulation.frontier == null ) {
		    GLogger.global.info("SimulationExplanation.makeBetaCall simulation.frontier == null");
		    System.exit(1);
		}

	
		Form newSituation = simulation.createAtom();
		Name newName = new Name();
		lexicon.put(newName, newSituation);
        ComplexTransform betaSystem = new ComplexTransform(newName);
        betaSystem.getGraph().addVertex(beta_in_node);
        // TODO add nodes to the betaSystem (mark exit nodes, too)
        for( TransformEdge arc : arcs ) {
            // TODO add arcs to the betaSystem
        }
        
        History betaHistory = new History();
        
        // TODO what is the traversal of betaCall ?? !!!!

        CallVertex betaCV = new CallVertex(betaHistory.nextVertexId(), betaSystem.start, source);
        ComplexTransformCall betaCall =
        	(ComplexTransformCall) betaSystem.constructCall(betaHistory.nextCallId(),
        			betaHistory, lexicon, null);
        betaCall.unify(betaHistory, betaCV);
        betaHistory.addCall(betaCall);

		if( ! betaCall.hasNext ) {
		    GLogger.global.info("SimulationExplanation.makeBetaCall the betaCall is immediately not active");
		}

        return betaCall;
    }
    
    public void next(History history, Lexicon lexicon)
    {
		steps++;
	
		GLogger.global.entering("SimulationExplanation", "next");

        int constraintIndex = 0;
        
        while( constraintIndex < simulation.constraints.size() ) {

	    GLogger.global.info("SimulationExplanation.next constraintIndex = " + constraintIndex);

            SimpleTransform constraint = simulation.constraints.get(constraintIndex);
            ComplexTransformCall betaCall = betaCallByConstraint.get(constraint);

            if( betaCall.hasNext ) {
				GLogger.global.info("SimulationExplanation.next betaCall.next()");
				betaCall.next(history, lexicon);
            }

            constraintIndex++;
        }

    }

    // For a SimulationExplanation to be "complete" means that
    // every betaCall (one per constraint) is done.
    // Of course, this says nothing about whether or not the betaCall
    // ended in an output state which matches what the constraint needs.
    // This is what constraintFits is for.

    public boolean hasNext()
    {
		GLogger.global.entering("SimulationExplanation", "hasNext");

        int constraintIndex = 0;
        
        while( constraintIndex < simulation.constraints.size() ) {

		    GLogger.global.info("SimulationExplanation.hasNext constraintIndex = " + constraintIndex);
            
            SimpleTransform constraint = simulation.constraints.get(constraintIndex);
            ComplexTransformCall betaCall = betaCallByConstraint.get(constraint);
            
            if( betaCall.hasNext ) {
				GLogger.global.info("SimulationExplanation.hasNext constraint " + constraintIndex + " is active.  returning true");
                return true;
            }
            
            constraintIndex++;
        }

		GLogger.global.info("SimulationExplanation.hasNext about to return false");

        return false;
    }
    
    public ComplexTransform constraintFits(SimpleTransform constraint, Lexicon lexicon)
    {
    	GLogger.global.entering("SimulationExplanation", "constraintFits");
    	
    	GLogger.global.info("SimulationExplanation.constraintFits constraint = " + constraint.toString() );
    	
    	// return the cached copy
    	// when is this ever called ???
    	ComplexTransform rs = betaSystemByConstraint.get(constraint);
    	if( rs != null ) {
    		GLogger.global.info("SimulationExplanation.constraintFits returning cached copy of the transform system");
    		return rs;
    	}
    	
    	ComplexTransformCall betaCall = betaCallByConstraint.get(constraint);
    	Form source = sourceByConstraint.get(constraint);
    	Form destination = destinationByConstraint.get(constraint);
    	
    	GLogger.global.info("SimulationExplanation.constraintFits source situation is " + source.toString());
    	GLogger.global.info("SimulationExplanation.constraintFits destination situation is " + destination.toString());
    	
    	for( CallVertex output : betaCall.getGraph().getVertices() ) {
    		
    		GLogger.global.info("SimulationExplanation.constraintFits checking to see if this output matches the destination: " + output.getForm().toString());
    		GLogger.global.info("SimulationExplanation.constraintFits betaCall output situation: " + output.getForm());
    		
    		if( output.getForm().equals(destination) ) {
    			
    			GLogger.global.info("SimulationExplanation.constraintFits it does");
    			
    			// TODO this first parameter (new Name()) is wrong !!!
    			rs = betaCall.getTransformSystemTo(new Name(), source, output);
    			
    			betaSystemByConstraint.put(constraint, rs);
    			
    			// TODO not sure if this is the best place to alter the newLexicon.  It may be that these
    			// TransformSystem's go unused, in which case we don't want to leave junk that refers to them
    			
    			lexicon.put(new Name((lexicon.getNameOf(constraint)).base + "_implementation"), rs);
    			lexicon.put(new Name((lexicon.getNameOf(constraint)).base + "_guard"), source);
    			
    			return rs;
    		}
    		else {
    			GLogger.global.info("SimulationExplanation.constraintFits it doesn't");
    		}
    	}
    	
    	return null;
    }
    
    public boolean fits(Lexicon lexicon)
    {
	GLogger.global.entering("SimulationExplanation", "fits");
	
	int constraintIndex = 0;
	while( constraintIndex < simulation.constraints.size() ) {
	    
            SimpleTransform constraint = simulation.constraints.get(constraintIndex);

	    if( constraintFits(constraint, lexicon) == null ) {
		return false;
	    }
	    
	    constraintIndex++;
	}

	return true;
    }

    public int compareTo(SimulationExplanation other)
    {
        return candidate.size().compareTo(other.candidate.size());
        // return candidate.size() + steps;
    }
    
    public String toString(Lexicon lexicon)
    {
        String result = "";
        
        int constraintIndex = 0;
        
        while( constraintIndex < simulation.constraints.size() ) {
            
            SimpleTransform constraint = simulation.constraints.get(constraintIndex);
            
            result += constraint.toString() + "\n";
            result += "\n";
            
            ComplexTransform rs = betaSystemByConstraint.get(constraint);
            if( rs == null ) {
                GLogger.global.info("SimulationExplanation.toString rs == null!");
            }
            else {
                result += "situation " + rs.guardName + " " + lexicon.getForm(rs.guardName).toString() + "\n";
                result += "\n";
                result += rs.toString() + "\n";
            }
            constraintIndex++;
        }
        
        return result;
    }
    
}
