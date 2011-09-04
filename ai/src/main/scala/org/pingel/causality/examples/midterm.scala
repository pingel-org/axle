package org.pingel.causality.examples

import org.pingel.causality.CausalModel
import org.pingel.bayes.ModelVisualizer

class midterm {
  
    def main(args: Array[String]) {
        
        val hw4 = new Homework4Model(5, 0.2)

        ModelVisualizer.draw(hw4)
    }
}
