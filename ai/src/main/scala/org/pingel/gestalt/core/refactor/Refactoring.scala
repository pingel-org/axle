
package org.pingel.gestalt.core.refactor

import org.pingel.gestalt.core.Lexicon

abstract case class Refactoring(lexicon: Lexicon) {

	def execute(): Unit
	
}
