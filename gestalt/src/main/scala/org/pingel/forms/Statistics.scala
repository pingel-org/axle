package org.pingel.forms

import org.pingel.gestalt.core.ComplexForm
import org.pingel.gestalt.core.FormFactory
import org.pingel.gestalt.core.Lambda
import org.pingel.gestalt.core.Name
import org.pingel.gestalt.core.SimpleForm

object Statistics {

  object CorrelationCoefficient extends FormFactory {

    //    RandomVariable X;
    //    RandomVariable Y;

    val X = new Name("X")
    val Y = new Name("Y")
    val lambda = new Lambda()
    lambda.add(X, new Name("RandomVariable"))
    lambda.add(Y, new Name("RandomVariable"))
    
    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("correlationCoefficient")), new SimpleForm(X)), new SimpleForm(Y), lambda)

    //    public Form reduce()
    //    {
    //        return new Quotient(new Covariance(X, Y), new Product(new LittleSigma(X), new LittleSigma(Y)));
    //    }
    //    
    //    public Form evaluate(ProbabilityTable t,
    //            Map<Variable, Form> values, VariableNamer namer)
    //    {
    //        Form reduced = reduce();
    //        return reduced.evaluate(t, values, namer);
    //    }
    //
    //    public String toLaTeX()
    //    {
    //        return "\\fract{\\sigma_{" + X.name + Y.name + "}}" + 
    //            "{\\sigma_{X}\\sigma_{Y}}";
    //    }

  }

  object Counterfactual extends FormFactory {

    //    RandomVariable Y;
    //    Variable x;

    val Y = new Name("Y")
    val x = new Name("x")
    val lambda = new Lambda()
    lambda.add(Y, new Name("RandomVariable"))
    lambda.add(x, new Name("Variable"))

    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("counterfactual")), new SimpleForm(Y)), new SimpleForm(x), lambda)

    //    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer) {
    //
    ////      TODO returns the "value that Y would have obtained had X been x"
    //        return null;
    //    }
    //
    //    public String toLaTeX()
    //    {
    //        return Y.name + "_{" + x.toLaTeX() + "}";
    //    }

  }

  object Covariance extends FormFactory {

    //    RandomVariable X;
    //    RandomVariable Y;

    val X = new Name("X")
    val Y = new Name("Y")
    val lambda = new Lambda()
    lambda.add(X, new Name("RandomVariable"))
    lambda.add(Y, new Name("RandomVariable"))

    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("covariance")), new SimpleForm(X)), new SimpleForm(Y), lambda)

    //    public Form reduce(VariableNamer namer)
    //    {
    //        // E[(X - E(X))(Y - E(Y))]
    //        
    //        Variable x1 = X.nextVariable(namer);
    //        Variable x2 = X.nextVariable(namer);
    //        Variable y1 = Y.nextVariable(namer);
    //        Variable y2 = Y.nextVariable(namer);
    //
    //        Set<Variable> outer = new HashSet<Variable>();
    //        outer.add(x1);
    //        outer.add(y1);
    //
    //        Set<Variable> inx = new HashSet<Variable>();
    //        inx.add(x2);
    //        
    //        Set<Variable> iny = new HashSet<Variable>();
    //        iny.add(y2);
    //        
    //        return new Expectation(outer, null,
    //                new Product(
    //                        new Difference(
    //                                x1,
    //                                new Expectation(inx, null, x2)),
    //                        new Difference(
    //                                y1,
    //                                new Expectation(iny, null, y2))));
    //    }

    //    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer) {
    //
    //        Form reduced = reduce(namer);
    //        return reduced.evaluate(t, values, namer);
    //    }
    //
    //    public String toLaTeX()
    //    {
    //        return "\\sigma_{" + X.name + Y.name + "}";
    //    }

  }

  object Entropy extends FormFactory {

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
    lambda.add(rv, new Name("RandomVariable"))
    lambda.add(base, new Name("Double"))

    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("entropy")), new SimpleForm(rv)), new SimpleForm(base), lambda)

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

  object Expectation extends FormFactory {
    
    var expectors: Set[Variable]
    var condition: Set[Variable]

    def createForm(expectors: Set[Variable], condition: Set[Variable], exp: Form): Form = {
      this.expectors = expectors
      if (condition == null) {
        this.condition = Set[Variable]()
      } else {
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

  object LittleSigma extends FormFactory {

    //    RandomVariable X;

    val X = new Name("X")
    val lambda = new Lambda()
    lambda.add(X, new Name("RandomVariable"))

    val archetype = new ComplexForm(new SimpleForm(new Name("littleSigma")), new SimpleForm(X), lambda)

    //    public Form reduce(VariableNamer namer)
    //    {
    //        // TODO I'm not sure if this is right.
    //        // I think I need absolute value of each term.
    //        
    //        Variable x1 = X.nextVariable(namer);
    //        Set<Variable> xSet1 = new HashSet<Variable>();
    //        xSet1.add(x1);
    //        
    //        Variable x2 = X.nextVariable(namer);
    //        Set<Variable> xSet2 = new HashSet<Variable>();
    //        xSet2.add(x2);
    //
    //
    //        return new Expectation(xSet1, null, new Difference(x1, new Expectation(xSet2, null, x2)));
    //    }
    //    
    //    public Form evaluate(ProbabilityTable t,
    //            Map<Variable, Form> values, VariableNamer namer)
    //    {
    //        // TODO would be more efficient not to recalculate E(X) for every value of x
    //        Form reduced = reduce(namer);
    //        return reduced.evaluate(t, values, namer);
    //    }
    //
    //    public String toLaTeX()
    //    {
    //        return "\\sigma_{" + X + "}";
    //    }

  }

  object RegressionCoefficient extends FormFactory {

    //    RandomVariable X;
    //    RandomVariable Y;

    val X = new Name("X")
    val Y = new Name("Y")
    val lambda = new Lambda()
    lambda.add(X, new Name("RandomVariable"))
    lambda.add(Y, new Name("RandomVariable"))

    val archetype = new ComplexForm(new ComplexForm(new SimpleForm(new Name("regressionCoefficient")), new SimpleForm(X)), new SimpleForm(Y), lambda)

    //    public Form reduce()
    //    {
    //        return new Quotient(new Covariance(X, Y), new Variance(Y));
    //    }
    //    
    //    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer) {
    //        
    //        Form reduced = reduce();
    //        return reduced.evaluate(t, values, namer);
    //    }
    //    
    //    public String toLaTeX()
    //    {
    //        return "r_{" + X + Y + "}";
    //    }

  }

  object Variance extends FormFactory {

    // public RandomVariable rv;

    val rv = new Name("rv")
    val lambda = new Lambda()
    lambda.add(rv, new Name("RandomVariable"))

    val archetype = new ComplexForm(new SimpleForm(new Name("variance")), new SimpleForm(rv), lambda)

    //    public Form reduce(VariableNamer namer)
    //    {
    //        // E[(X - E(X))^2]
    //        Variable observation1 = rv.nextVariable(namer);
    //        Set<Variable> obsSet1 = new HashSet<Variable>();
    //        obsSet1.add(observation1);
    //        
    //        Variable observation2 = rv.nextVariable(namer);
    //        Set<Variable> obsSet2 = new HashSet<Variable>();
    //        obsSet2.add(observation2);
    //        
    //        return new Expectation(obsSet1, null, new Square(new Difference(observation1, new Expectation(obsSet2, null, observation2))));
    //    }

    //    public Form evaluate(ProbabilityTable t, Map<Variable, Form> values, VariableNamer namer) {
    //
    //        // TODO not efficient since we could cache E(X)
    //        
    //        Form reduced = reduce(namer);
    //        return reduced.evaluate(t, values, namer);
    //    }
    //    
    //    public String toLaTeX()
    //    {
    //        return "\\sigma^2_{" + rv.name + "}";
    //    }

  }

}
