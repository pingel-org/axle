package org.pingel.gestalt.core.refactor;

import org.pingel.gestalt.core.Lexicon;
import org.pingel.gestalt.core.Name;

public class RenameTransformRefactoring extends Refactoring {

	Name from;
	Name to;
	
	public RenameTransformRefactoring(Lexicon lexicon, Name from, Name to)
	{
		super(lexicon);
		this.from = from;
		this.to = to;
	}

	public void execute()
	{
		lexicon.renameTransform(from, to);
	}
	
}
