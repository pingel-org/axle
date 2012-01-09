package org.pingel.gestalt.core

import java.io.FileReader
import java.io.IOException
import java.io.InputStream
import java.io.Reader

import org.pingel.gestalt.parser.GestaltParser
import org.pingel.gestalt.parser.ParseException
import org.pingel.gestalt.parser.StaticAnalyzingVisitor
import org.pingel.gestalt.parser.syntaxtree.Goal

import scala.collection._

object Common {
	
	var parser: GestaltParser = null
	
	val included = Set[String]()
	
	def processFile(name: String, lexicon: Lexicon): List[String] = {
		val gestaltHome = System.getProperty("GESTALT_HOME")
		val separator = System.getProperty("file.separator")
		val fullFilename = gestaltHome + separator + "cl" + separator + name + ".cl"
		GLogger.global.info("parsing " + fullFilename)
		var goal = null
		try {
			var rdr = new FileReader(fullFilename)
			if( parser == null ) {
				parser = new GestaltParser(rdr)
			}
			else {
				GestaltParser.ReInit(rdr)
			}
			goal = GestaltParser.Goal()
			rdr.close()
		}
		catch {
		  case e: ParseException => {
			GLogger.global.severe("Encountered errors during parse\n" + e)
			System.exit(1)
		  }
		  case e2: IOException => {
			GLogger.global.severe("IO Exception " + e2)
			throw(e2)
		  }
		}
		var v = new StaticAnalyzingVisitor(lexicon)
		goal.accept(v, null)
		v.getIncludes()
	}
	
	def processStream(in: InputStream, lexicon: Lexicon): List[String] = {
		var goal: Goal = null
		try {
			if( parser == null ) {
				parser = new GestaltParser(in)
			}
			else {
				GestaltParser.ReInit(in)
			}
			goal = GestaltParser.Goal()
		}
		catch {
		  case e: ParseException => {
			GLogger.global.severe("Encountered errors during parse\n" + e)
			System.exit(1)
		  }
		}
		
		var v = new StaticAnalyzingVisitor(lexicon)
		goal.accept(v, null)
		v.getIncludes()
	}
	
	
	def include(includes: List[String], lexicon: Lexicon): Unit = {
		// BFS traversal
		// TODO add cycle detection
		while( includes.size > 0 ) {
			val include = includes.remove(0)
			if( ! included.contains(include) ) {
				included += include
				val newIncludes = Common.processFile(include, lexicon)  // or pass a new newLexicon???
				includes ++= newIncludes
			}
		}
	}
	
	def include(inc: String, lexicon: Lexicon): Unit = {
		println("including " + inc)
		var includes = mutable.ListBuffer[String]()
		includes += inc
		include(includes, lexicon)
	}
	
	def include(in: InputStream, lexicon: Lexicon): Unit = {
		var includes = Common.processStream(in, lexicon)
		include(includes, lexicon)
	}
	
}
