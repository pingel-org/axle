
package org.pingel.forms.stats

import java.util.HashSet
import java.util.Set

import org.pingel.forms.Variable
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.FormFactory

class Expectation extends FormFactory
{
    val expectors: Set[Variable]
    val condition: Set[Variable]
    
    def createForm(expectors: Set[Variable], condition: Set[Variable], exp: Form): Form =  {
        this.expectors = expectors
        if( condition == null ) {
            this.condition = Set[Variable]()
        }
        else {
            this.condition = condition
        }
        this.exp = exp
    }

//    public Form reduce()
//    {
//        // E[f(X)] -> Sigma_x [f(x)P(x)]
//        // E[X|y] -> 
//        
//        List<Variable> iterated = new Vector<Variable>();
//        iterated.addAll(expectors);
//        Set<Variable> iteratedSet = new HashSet<Variable>();
//        iteratedSet.addAll(expectors);
//        return new Sigma(iterated, new Product(exp, new Probability(iteratedSet, condition, new HashSet<Variable>())));
//    }
//    
//    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer)
//    {
//        Form reduced = reduce();
//        return reduced.evaluate(t, values, namer);
//    }
//
//    public String toLaTeX()
//    {
//        // TODO include iterated vars ?
//        return "E[" + exp.toLaTeX() + "]";
//    }
    
}
