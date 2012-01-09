
package org.pingel.gestalt.core.refactor;

import org.pingel.gestalt.core.Lexicon;

abstract public class Refactoring {

	public Lexicon lexicon;
	
	Refactoring(Lexicon lexicon)
	{
		this.lexicon = lexicon;
	}
	
	public abstract void execute();
	
}
