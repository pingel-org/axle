package org.pingel.bayes;

import java.util.HashSet;
import java.util.Set;

import org.pingel.forms.Variable;
import org.pingel.gestalt.core.Form;


public class Probability
{
    // For now I'll just use sets of variables instead of assignments.
    // I'm confused about what the best representation for this stuff is.
    
	private Set<Variable> question;
    private Set<Variable> given;
    private Set<Variable> actions;

//	public Name question = new Name("question");
//	public Name given = new Name("given");
//	public Name actions = new Name("actions");

//	public Probability()
//    {
//		Lambda lambda = new Lambda();
//		lambda.add(question);
//		lambda.add(given);
//		lambda.add(actions);
//
//		archetype = new ComplexForm(new ComplexForm(new ComplexForm(new SimpleForm(new Name("probability")), new SimpleForm(question)), new SimpleForm(given)), new SimpleForm(actions), lambda);
//    	
//    }
    
    public Probability(Set<Variable> question)
    {
        this.question = question;
        this.given = new HashSet<Variable>();
        this.actions = new HashSet<Variable>();
    }

    public Probability(Set<Variable> question, Set<Variable> given)
    {
        this.question = question;
        this.given = given;
        this.actions = new HashSet<Variable>();
    }

    public Probability(Set<Variable> question, Set<Variable> given, Set<Variable> actions)
    {
        this.question = question;
        this.given = given;
        this.actions = actions;
    }

    public Set<Variable> getQuestion()
    {
        Set<Variable> result = new HashSet<Variable>();
        result.addAll(question);
        return result;
    }

    public Set<Variable> getGiven()
    {
        Set<Variable> result = new HashSet<Variable>();
        result.addAll(given);
        return result;
    }
    
    public Set<Variable> getActions()
    {
        Set<Variable> result = new HashSet<Variable>();
        result.addAll(actions);
        return result;
    }

    public int getActionSize(Form f)
    {
    		return -1; // TODO !!!
    }
    
//    public Case getConditionCase(Map<Variable, DoubleValue> values)
//    {
//    		Case condition = new Case();
//    	
//    		for( Variable var : question ) {
//    			RandomVariable rv = var.getRandomVariable();
//    			condition.assign(rv, values.get(var));
//    		}
//    	
//    		return condition;
//    }
//    
//    public Case getPriorCase(Map<Variable, DoubleValue> values)
//    {
//    		Case prior = new Case();
//    	
//    		for( Variable g : given ) {
//    			RandomVariable rv = g.getRandomVariable();
//    			prior.assign(rv, values.get(g));
//    		}
//    	
//    		for( Variable a : actions ) {
//    			RandomVariable rv = a.getRandomVariable();
//    			prior.assign(rv, values.get(a));
//    		}
//    	
//    		return prior;
//    }
    
//    public Form evaluate(Factor t, Map<Variable, Form> var2value, VariableNamer namer)
//    {
//    		Case condition = getConditionCase(var2value);
//    		Case prior = getPriorCase(var2value);
//    		DoubleValue doubleValueFactory = new DoubleValue();
//    		Form dbl = doubleValueFactory.createDoubleValue(t.evaluate(condition, prior));
//    		
//    		return dbl;
//    }

    
//    public String toLaTeX()
//    {
//        String result = "P(";
//
//        result += Stringer.render(question, ", ");
//
//        if( ! given.isEmpty() || ! actions.isEmpty() ) {
//        	result += " | ";
//        }
//        
//        Iterator<Variable> evit = given.iterator();
//        while( evit.hasNext() ) {
//            Variable g = evit.next();
//            result += g.toLaTeX();
//            if( evit.hasNext() || actions.size() > 0) {
//                result += ", ";
//            }
//        }
//
//        Iterator<Variable> ait = actions.iterator();
//        while( ait.hasNext() ) {
//            Variable a = ait.next();
//            result += "\\hat{" + a.toLaTeX() + "}";
//            if( ait.hasNext() ) {
//                result += ", ";
//            }
//        }
//        
//        return result + ")";
//    }

}
