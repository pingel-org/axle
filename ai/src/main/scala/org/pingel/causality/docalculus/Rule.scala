
package org.pingel.causality.docalculus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pingel.bayes.CausalModel;
import org.pingel.bayes.Probability;
import org.pingel.bayes.RandomVariable;
import org.pingel.bayes.VariableNamer;
import org.pingel.forms.Variable;
import org.pingel.gestalt.core.Form;

abstract public class Rule
{
    public abstract List<Form> apply(Probability q, CausalModel m, VariableNamer namer);
    
    protected Set<RandomVariable> randomVariablesOf(Set<Variable> variables)
    {
        Set<RandomVariable> result = new HashSet<RandomVariable>();
        for( Variable v : variables ) {
            result.add(v.getRandomVariable());
        }
        return result;
    }
}
