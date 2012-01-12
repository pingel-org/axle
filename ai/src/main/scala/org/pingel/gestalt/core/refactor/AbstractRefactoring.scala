package org.pingel.gestalt.core.refactor

import org.pingel.gestalt.core.Lexicon

case class AbstractRefactoring(lexicon: Lexicon)
extends Refactoring(lexicon) 
{
	def execute(): Unit = {
	  
	}
}
