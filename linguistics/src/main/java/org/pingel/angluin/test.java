package org.pingel.angluin;
public class test
{
    
    public final static void main(String[] args)
    {
        Alphabet Sigma = new Alphabet();
        
        Symbol mHi = new Symbol("hi", Sigma);
        Symbol mIm = new Symbol("I'm", Sigma);
        Symbol mYour = new Symbol("your", Sigma);
        Symbol mMother = new Symbol("Mother", Sigma);
        Symbol mShut = new Symbol("shut", Sigma);
        Symbol mUp = new Symbol("up", Sigma);
        
        Expression s1 = new Expression();
        s1.addSymbol(mHi);
        s1.addSymbol(mIm);
        s1.addSymbol(mYour);
        s1.addSymbol(mMother);
        
        Expression s2 = new Expression();
        s2.addSymbol(mShut);
        s2.addSymbol(mUp);
        
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
