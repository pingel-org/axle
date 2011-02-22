package org.pingel.gold;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class Vocabulary
{
    private Set<Morpheme> morphemes;

    public Vocabulary()
    {
        morphemes = new HashSet<Morpheme>();
    }

    public void addMorpheme(Morpheme m)
    {
        morphemes.add(m);
    }

    public Iterator<Morpheme> iterator()
    {
        return morphemes.iterator();
    }

    

}
