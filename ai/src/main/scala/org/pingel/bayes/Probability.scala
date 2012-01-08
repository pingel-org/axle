package org.pingel.bayes

import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form


class Probability(question: Set[Variable], given: Set[Variable]=Set(), actions: Set[Variable]=Set()) {
    // For now I'll just use sets of variables instead of assignments.
    // I'm confused about what the best representation for this stuff is.

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
   

  def getQuestion() = question
  def getGiven() = given
  def getActions() = actions

  def getActionSize(f: Form): Integer = -1 // TODO !!!
    
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
