package org.pingel.gestalt.core.refactor

import org.pingel.gestalt.core.Lexicon
import org.pingel.gestalt.core.Name

case class RenameTransformRefactoring(lexicon: Lexicon, from: Name, to: Name)
extends Refactoring(lexicon) 
{
	def execute(): Unit = {
		lexicon.renameTransform(from, to)
	}
	
}
