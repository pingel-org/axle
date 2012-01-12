package org.pingel.gestalt.core.refactor

import org.pingel.gestalt.core.Lexicon

case class InlineRefactoring(lexicon: Lexicon)
extends Refactoring(lexicon) 
{
	def execute(): Unit = {
	}
}
