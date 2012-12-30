package org.pingel.gestalt.core.refactor

import org.pingel.gestalt.core.Lexicon

case class InlineRefactoring(override val lexicon: Lexicon)
  extends Refactoring(lexicon) {
  def execute(): Unit = {
  }
}
