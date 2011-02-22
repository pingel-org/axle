package org.pingel.forms.logic;

import org.pingel.gestalt.core.ComplexForm;
import org.pingel.gestalt.core.FormFactory;
import org.pingel.gestalt.core.Lambda;
import org.pingel.gestalt.core.Name;
import org.pingel.gestalt.core.SimpleForm;

public class ModelsOf extends FormFactory {


	public ModelsOf()
	{
		Name arg1 = new Name("arg1");
		Lambda lambda = new Lambda();
		lambda.add(arg1);

		archetype = new ComplexForm(new SimpleForm(new Name("modelsOf")), new SimpleForm(arg1), lambda);
	}
	
//	public Type getType() {
//		return new Function(new Function(new UnknownType(), new Booleans()), new Set(new Model()));
//	}
//
//	public Form evaluate(ProbabilityTable t, Map<Variable, Form> values,
//			VariableNamer namer) {
//		// TODO
//		return null;
//	}
//
//	public String toLaTeX() {
//		// TODO
//		return null;
//	}

}
