package org.pingel.causality.examples;


import org.pingel.bayes.CausalModel;
import org.pingel.bayes.ModelVisualizer;

public class midterm
{
    public static void main(String[] args) {
        
        CausalModel hw4 = new Homework4Model(5, 0.2);

        ModelVisualizer.draw(hw4);
    }
}
