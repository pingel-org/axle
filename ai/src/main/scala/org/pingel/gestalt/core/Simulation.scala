package org.pingel.gestalt.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Simulation
{
    // the problem description:
    public List<Name> goals;
    public List<SimpleTransform> constraints;
    public List<Form> forms;
    public List<Transform> transforms;

    // the working variables:
    public SimulationFrontier frontier;

    private boolean complete = false;

    public Simulation(List<Name> goals,
                      List<SimpleTransform> constraints,
                      List<Form> forms,
                      List<Transform> transforms)
    {
		this.goals = goals;
		this.constraints = constraints;
		this.forms = forms;
		this.transforms = transforms;
    }

	public void initialize(Lexicon lexicon) {
		frontier = new SimulationFrontier();
		frontier.initialize(this, lexicon);
	}

	public SimpleForm createAtom()
	{
		Name name = new Name();
		// Note: this name is free !!
		return new SimpleForm(name, null);
	}

	public ComplexForm createComplex()
	{
		return new ComplexForm(createAtom(), createAtom(), null);
	}

    public boolean hasNext()
    {
        return (! complete);
    }

    public void next(History history, Lexicon lexicon)
    {
        Iterator<Name> goalIt = goals.iterator();
        while( goalIt.hasNext() ) {
	    
            Name goal = goalIt.next();
	    
            SimulationExplanation explanation = frontier.smallest(goal);
            
            Logger.global.info("Simulation.next explanation.candidate " + explanation.candidate.toString());
	    	Logger.global.info("Simulation.next before calls to explanation.next()");
	    
		    explanation.next(history, lexicon);

	    	Logger.global.info("Simulation.next after call to explanation.next()");
	    
            if ( ! explanation.hasNext() ) {

				frontier.remove(goal, explanation);

				if( explanation.fits(lexicon) ) {
                
				    System.out.println();
				    System.out.println("goal is satisfied by: " + explanation.candidate.toString());
				    System.out.println();
				    System.out.println("explanation:");
				    System.out.println(explanation.toString(lexicon));
			
				    complete = true;
				}
				else {
		
				    // Here we pass the explanation.candidate, rather than the whole explanation
				    // because it seems that "explore" doesn't need the entire explanation.  This
				    // may change.
					
					CandidateExploration exploration =
						new CandidateExploration(this, goal, explanation.candidate);

					while( exploration.hasNext() ) {
						exploration.next(history, lexicon);
					}
				}

            }
	    
            // TODO note somewhere (explored_by_goal) that candidate has been explored
        }

    }

	class CandidateExploration  {

		private Simulation simulation;
		private Name goal;
		private Form candidate;		

		private Set variables;
		private Iterator<Name> variable_it;

		CandidateExploration(Simulation simulation, Name goal, Form candidate) {

			this.simulation = simulation;
			this.goal = goal;
			this.candidate = candidate;

			// TODO this will change if the semantics of "scopedVars" changes
			// (I imagine someday scoping will happen elsewhere)
			variable_it = candidate.lambda.getNames().iterator();

		}

		public void next(History history, Lexicon lexicon) {
        
			Name variable = variable_it.next();
            
			Logger.global.info("Simulation.explore variable is " + variable);
            
			int literal_index = 0;
			while( literal_index < forms.size() ) {
                
				Form literal = forms.get(literal_index);
                
				Map<Name, Form> replacement_map = new TreeMap<Name, Form>();
				replacement_map.put(variable, literal);
		
				frontier.add(goal,
									new SimulationExplanation(simulation,
													   goal,
													   candidate.duplicateAndReplace(replacement_map), lexicon));
                	
				literal_index++;
			}
            	
			Map<Name, Form> replacement_map = new TreeMap<Name, Form>();
			replacement_map.put(variable, createComplex());
			frontier.add(goal,
						 new SimulationExplanation(simulation,
						   						   goal,
												   candidate.duplicateAndReplace(replacement_map), lexicon));

		}

		public boolean hasNext() {
			return( variable_it.hasNext() );
		}
		
	}
}
