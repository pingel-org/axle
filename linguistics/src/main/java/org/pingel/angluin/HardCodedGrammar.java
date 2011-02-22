package org.pingel.angluin;
public class HardCodedGrammar implements Grammar
{
    private Language L;
    
    public HardCodedGrammar(Language L)
    {
        this.L = L;
    }
    
    public Language L()
    {
        return L;
    }
    
}
