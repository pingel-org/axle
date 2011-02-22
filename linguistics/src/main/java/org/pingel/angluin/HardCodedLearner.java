package org.pingel.angluin;
public class HardCodedLearner extends Learner
{
    private Grammar G;
    
    public HardCodedLearner(Text T, Grammar G)
    {
        super(T);
        this.G = G;
    }
    
    public Grammar processNextExpression()
    {
        Expression s = nextExpression();
        
        return G;
    }
    
}
