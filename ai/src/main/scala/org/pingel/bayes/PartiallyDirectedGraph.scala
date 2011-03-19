
package org.pingel.bayes

class PartiallyDirectedGraph
{
	private static final Boolean TRUE = new Boolean(true);
	private static final Boolean FALSE = new Boolean(false);
	
	private List<RandomVariable> variables;

	private Map<RandomVariable, Integer> variable2index = new HashMap<RandomVariable, Integer>();
	
	private boolean[][] connect;
	private boolean[][] mark;
	private boolean[][] arrow;
	
	public PartiallyDirectedGraph(List<RandomVariable> vars)
	{
		this.variables = vars;
		connect = new boolean[vars.size()][vars.size()];
		mark = new boolean[vars.size()][vars.size()];
		arrow = new boolean[vars.size()][vars.size()];
		for(int i=0; i < vars.size(); i++) {
			variable2index.put(vars.get(i), new Integer(i));
			for(int j=0; j < vars.size(); j++) {
				connect[i][j] = false;
				mark[i][j] = false;
				arrow[i][j] = false;
			}
		}
	}

	private int indexOf(RandomVariable v)
	{
		return variable2index.get(v).intValue();
	}
	
    public void connect(RandomVariable v1, RandomVariable v2)
    {
    	int i1 = indexOf(v1);
    	int i2 = indexOf(v2);
    	connect[i1][i2] = true;
    	connect[i2][i1] = true;
    }

    public boolean areAdjacent(RandomVariable v1, RandomVariable v2)
    {
    	int i1 = indexOf(v1);
    	int i2 = indexOf(v2);
    	return connect[i1][i2];
    }

    public boolean undirectedAdjacent(RandomVariable v1, RandomVariable v2)
    {
    	int i1 = indexOf(v1);
    	int i2 = indexOf(v2);

    	return connect[i1][i2] && ! arrow[i1][i2] && ! arrow[i2][i1];
    	
    }

    public void mark(RandomVariable v1, RandomVariable v2)
    {
    	int i1 = indexOf(v1);
    	int i2 = indexOf(v2);
    	mark[i1][i2] = true;
    	mark[i2][i1] = true;
    }
    
    public void orient(RandomVariable v1, RandomVariable v2) {

    	// Note: we assume they are already adjacent without checking

    	int i1 = indexOf(v1);
    	int i2 = indexOf(v2);
        
        arrow[i1][i2] = true;
    }



    public Vector<RandomVariable> links(RandomVariable v, Boolean arrowIn, Boolean marked, Boolean arrowOut)
    {
    	Vector<RandomVariable> result = new Vector<RandomVariable>();
    	
    	int i = indexOf(v);
    	
    	for(int j=0; j < variables.size(); j++) {

    		RandomVariable u = variables.get(j);

    		boolean pass = connect[i][j];

    		if( pass && (arrowIn != null) )
    		{
    			pass = ( arrowIn.booleanValue() == arrow[j][i] );
    		}

    		if( pass && (marked != null) ) {
    			pass = ( marked.booleanValue() == mark[i][j] );
    		}

    		if( pass && (arrowOut != null) ) {
    			pass = ( arrowOut.booleanValue() == arrow[i][j] );
    		}
    		
    		if( pass ) {
    			result.add(u);
    		}
    	}

    	return result;
    }

    public boolean markedPathExists(RandomVariable from, RandomVariable target)
    {
    	// this will not terminate if there are cycles

    	Vector<RandomVariable> frontier = new Vector<RandomVariable>();
    	frontier.add(from);
    	
    	while( frontier.size() > 0 ) {
    		RandomVariable head = frontier.firstElement();
    		frontier.removeElementAt(0);
    		if( head.equals(target) ) {
    			return true;
    		}
    		Vector<RandomVariable> follow = links(head, null, TRUE, TRUE);
    		frontier.addAll(follow);
    	}
    	return false;
    }
    
    public String toString()
    {
        
//      private boolean[][] connect;
//      private boolean[][] mark;
//      private boolean[][] arrow;

        String result = "";

        for( RandomVariable rv : variables ) {
            result += "var " + rv + " has index " + variable2index.get(rv).toString();
            result += "\n";
        }
        
        result += "connect\n\n";
        
        for(int i=0; i < variables.size(); i++) {
            for(int j=0; j < variables.size(); j++) {
                result += connect[i][j] ? "x" : " ";
            }
            result += "\n";
        }
        result += "\n\n";
        
        result += "mark\n\n";
        
        for(int i=0; i < variables.size(); i++) {
            for(int j=0; j < variables.size(); j++) {
                result += mark[i][j] ? "x" : " ";
            }
            result += "\n";
        }
        result += "\n\n";
        
        result += "arrow\n\n";
        
        for(int i=0; i < variables.size(); i++) {
            for(int j=0; j < variables.size(); j++) {
                result += arrow[i][j] ? "x" : " ";
            }
            result += "\n";
        }
        result += "\n\n";

        return result;
    }
}
