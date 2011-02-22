package org.pingel.gold;
public class MemorizingLearner extends Learner
{
    private Language runningGuess = new Language();

    MemorizingLearner(Text T)
    {
	super(T);
    }

    public Grammar processNextExpression()
    {
	Expression s = nextExpression();

	if( ! ( s instanceof Hatch ) ) {
	    runningGuess.addExpression(s);
	}

	return new HardCodedGrammar(runningGuess);
    }

}
