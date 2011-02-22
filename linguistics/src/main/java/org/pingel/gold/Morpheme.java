

package org.pingel.gold;
public class Morpheme
{
    private String s;

    Morpheme(String s, Vocabulary vocabulary)
    {
	this.s = s;
	vocabulary.addMorpheme(this);
    }

    public String toString()
    {
	return s;
    }

}
