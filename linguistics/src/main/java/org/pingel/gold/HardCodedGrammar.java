package org.pingel.gold;
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
