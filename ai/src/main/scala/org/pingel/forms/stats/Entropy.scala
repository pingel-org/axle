package org.pingel.forms.stats;

import org.pingel.gestalt.core.ComplexForm
import org.pingel.gestalt.core.FormFactory
import org.pingel.gestalt.core.Lambda
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm

class Entropy extends FormFactory
{

//    RandomVariable rv;
//    Form base;

// TODO generalize the argument
    
//    public Form createForm(RandomVariable rv)
//    {
//        this.rv = rv;
//        base = new DoubleValue(2.0);
//    }

	val rv = new Name("rv")
	val base = new Name("base")
	val lambda = new Lambda()
	lambda.add(rv, new Name("RandomVariable"));
	lambda.add(base, new Name("Double"));
	
	archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("entropy")), new SimpleForm(rv)), new SimpleForm(base), lambda)

//    private Form reduce(VariableNamer namer)
//    {
//        Variable var = rv.nextVariable(namer);
//
//        List<Variable> varList = new Vector<Variable>();
//        varList.add(var);
//        
//        Set<Variable> varSet = new HashSet<Variable>();
//        varSet.add(var);
//        
//        Probability pOfX = new Probability(varSet);
//        
//        return new Negation(new Sigma(varList, new Product(pOfX, new Logarithm(base, pOfX))));
//    }
//    
//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer)
//    {
//        Form reduced = reduce(namer);
//        return reduced.evaluate(t, values, namer);
//    }
//
//    public String toLaTeX()
//    {
//        // TODO add base if specified
//        
//        return "H(" + rv.name + ")";
//    }
    
}
