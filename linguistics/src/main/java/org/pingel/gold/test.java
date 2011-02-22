package org.pingel.gold;
public class test
{

    public final static void main(String[] args)
    {
	Vocabulary Sigma = new Vocabulary();

	Morpheme mHi = new Morpheme("hi", Sigma);
	Morpheme mIm = new Morpheme("I'm", Sigma);
	Morpheme mYour = new Morpheme("your", Sigma);
	Morpheme mMother = new Morpheme("Mother", Sigma);
	Morpheme mShut = new Morpheme("shut", Sigma);
	Morpheme mUp = new Morpheme("up", Sigma);

	Expression s1 = new Expression();
	s1.addMorpheme(mHi);
	s1.addMorpheme(mIm);
	s1.addMorpheme(mYour);
	s1.addMorpheme(mMother);

	Expression s2 = new Expression();
	s2.addMorpheme(mShut);
	s2.addMorpheme(mUp);

	Language L = new Language();
	L.addExpression(s1);
	L.addExpression(s2);

	Text T = new Text();
	T.addExpression(s1);
	T.addExpression(new Hatch());
	T.addExpression(new Hatch());
	T.addExpression(s2);
	T.addExpression(new Hatch());
	T.addExpression(s2);
	T.addExpression(s2);

	System.out.println("Text T = " + T.toString());
	System.out.println("Language L = " + L.toString());
	System.out.println();
	
	boolean isFor = T.isFor(L);
	if( isFor ) {
	    System.out.println("T is for L");
	}
	else {
	    System.out.println("T is not for L");
	}
	System.out.println();

	Learner phi = new MemorizingLearner(T);

	Grammar guess = null;
	
	while( phi.hasNextExpression() ) {
	    
	    guess = phi.processNextExpression();

	    if( guess != null ) {

		Language guessedLanguage = guess.L();

		System.out.println("phi.processNextExpression().L = " + guessedLanguage.toString());

		if( guessedLanguage.equals(L) ) {
		    System.out.println("phi identified the language using the text");
		    System.exit(0);
		}
		else {
		    System.out.println("phi's guess was not correct\n");
		}
	    }

	}

	if ( guess == null ) {
	    System.out.println("phi never made a guess");
	}

    }

}
