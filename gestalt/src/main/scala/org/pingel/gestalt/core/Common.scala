package org.pingel.gestalt.core

import java.io.FileReader
import java.io.IOException
import java.io.InputStream
import java.io.Reader

import org.pingel.gestalt.parser.GestaltParser
import org.pingel.gestalt.parser.ParseException
import org.pingel.gestalt.parser.StaticAnalyzingVisitor
import org.pingel.gestalt.parser.syntaxtree.Goal

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

object Common {
	
	var parser: GestaltParser = null
	
	val included = Set[String]()
	
	def processFile(name: String, lexicon: Lexicon): ListBuffer[String] = {
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

		var result = new ListBuffer[String]()
		var v = new StaticAnalyzingVisitor(lexicon)
		goal.accept(v, null)
		result ++= v.getIncludes().toList
		result
	}
	
	def processStream(in: InputStream, lexicon: Lexicon): ListBuffer[String] = {
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

		var result = new ListBuffer[String]()
		var v = new StaticAnalyzingVisitor(lexicon)
		goal.accept(v, null)
		result ++= v.getIncludes().toList
		result
	}
	
	
	def includeListBuffer(includes: ListBuffer[String], lexicon: Lexicon): Unit = {
		// BFS traversal
		// TODO add cycle detection
		while( includes.size > 0 ) {
			val include = includes.remove(0)
			if( ! included.contains(include) ) {
				includes += include
				val newIncludes = Common.processFile(include, lexicon)  // or pass a new newLexicon???
				includes ++= newIncludes
			}
		}
	}
	
	def includeString(inc: String, lexicon: Lexicon): Unit = {
		println("including " + inc)
		var includes = ListBuffer[String]()
		includes += inc
		includeListBuffer(includes, lexicon)
	}
	
	def include(in: InputStream, lexicon: Lexicon): Unit = {
		var includes = Common.processStream(in, lexicon)
		includeListBuffer(includes, lexicon)
	}
	
}
