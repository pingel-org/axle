package org.pingel.gestalt.core;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.pingel.gestalt.parser.GestaltParser;
import org.pingel.gestalt.parser.ParseException;
import org.pingel.gestalt.parser.StaticAnalyzingVisitor;
import org.pingel.gestalt.parser.syntaxtree.Goal;

public class Common {
	
	private static GestaltParser parser;
	
	private static Set<String> included = new TreeSet<String>();
	
	private static List<String> processFile(String name, Lexicon lexicon)
	throws IOException
	{
		String gestaltHome = System.getProperty("GESTALT_HOME");
		String separator = System.getProperty("file.separator");
		String fullFilename = gestaltHome + separator + "cl" + separator + name + ".cl";
		GLogger.global.info("parsing " + fullFilename);
		
		Goal goal = null;
		
		try {
			Reader rdr = new FileReader(fullFilename);
			if( parser == null ) {
				parser = new GestaltParser(rdr);
			}
			else {
				GestaltParser.ReInit(rdr);
			}
			goal = GestaltParser.Goal();
			rdr.close();
		}
		catch (ParseException e) {
			GLogger.global.severe("Encountered errors during parse\n" + e);
			System.exit(1);
		}
		catch (IOException e2) {
			GLogger.global.severe("IO Exception " + e2);
			throw(e2);
		}
		
		StaticAnalyzingVisitor v = new StaticAnalyzingVisitor(lexicon);
		
		goal.accept(v, null);
		
		return v.getIncludes();
	}
	
	private static List<String> processStream(InputStream in, Lexicon lexicon)
	{
		Goal goal = null;
		
		try {
			if( parser == null ) {
				parser = new GestaltParser(in);
			}
			else {
				GestaltParser.ReInit(in);
			}
			
			goal = GestaltParser.Goal();
		}
		catch (ParseException e) {
			GLogger.global.severe("Encountered errors during parse\n" + e);
			System.exit(1);
		}
		
		StaticAnalyzingVisitor v = new StaticAnalyzingVisitor(lexicon);
		
		goal.accept(v, null);
		
		return v.getIncludes();
	}
	
	
	private static void include(List<String> includes, Lexicon lexicon)
	throws IOException
	{
		// BFS traversal
		// TODO add cycle detection
		while( includes.size() > 0 ) {
			String include = includes.remove(0);
			if( ! included.contains(include) ) {
				included.add(include);
				List<String> newIncludes = Common.processFile(include, lexicon);  // or pass a new newLexicon???
				includes.addAll(newIncludes);
			}
		}
	}
	
	public static void include(String include, Lexicon lexicon)
	throws IOException
	{
		System.out.println("including " + include);
		
		List<String> includes = new Vector<String>();
		includes.add(include);
		include(includes, lexicon);
	}
	
	public static void include(InputStream in, Lexicon lexicon)
	throws IOException
	{
		List<String> includes = Common.processStream(in, lexicon);
		include(includes, lexicon);
	}
	
}
