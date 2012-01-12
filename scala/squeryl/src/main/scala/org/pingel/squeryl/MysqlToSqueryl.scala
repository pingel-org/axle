
package org.pingel.squeryl

import java.sql.Connection

import scala.collection._

import org.pingel.squeryl.InformationSchema._
import org.pingel.squeryl.information_schema.entities._
import org.squeryl.PrimitiveTypeMode._

trait Ast {
  def toScala(): String
}

class AstPackage(pkg: String) extends Ast {
  override def toScala() = {
    "package " + pkg
  }
}

class AstImport(pkg: String, underscore: Boolean=false, atoms: List[String]=Nil) extends Ast {    
  
  override def toScala() = {
	  "import " + pkg + 
		  	(underscore match {
		  	    case true => "._"
                case _ => ""
		    }) +
		    (atoms match {
		      case Nil => ""
		      case _ => "{"+atoms.mkString(",")+"}"
		    })
  }
  
}

class AstClassDef(name: String, isCase: Boolean=false) {

  override def toScala() = {

	 (isCase match {
	   case true => "case "
	   case _ => ""
	 }) +
	 name +
	 "(\n" + asdf +
	 ")" + "\n{" +
	 "}"
  }
  
}


class MysqlToSqueryl() {

  // def getX = x.lookup(id)

  def getTableBySchemaAndName(schema: String, name: String) =
    tables.where( t => t.TABLE_NAME === name and t.TABLE_SCHEMA === schema ).single

  def getColumnsByTable(t: Tables) = 
    columns.where( c => c.TABLE_NAME === t.TABLE_NAME and c.TABLE_SCHEMA === t.TABLE_SCHEMA ) // TODO: orderBy(c.ORDINAL_POSITION asc)

  def scalaTypeFor(c: Columns) = "String" // TODO

  def scalaInitArgFor(c: Columns) = "None" // TODO

  def scalaForColumnDefinition(c: Columns) = "  var " + c.COLUMN_NAME + ": " + scalaTypeFor(c)

  def scalaForTable(t: Tables, pkg: String) = {

    val cols = getColumnsByTable(t)

    // val scala = new mutable.ListBuffer[Ast]()

    val s = List[Ast](
      new AstPackage(pkg),
      new AstImport("org.squeryl", true),
      new AstImport("org.squeryl.PrimitiveTypeMode", true),
      new AstImport("org.squeryl.annotations.Column", false),
      new AstImport("org.squeryl.adapters.MySQLAdapter", false),
      new AstImport("java.sql", true), // TODO compute only what's needed
      new AstClassDef(t.TABLE_NAME.toUpperCase, true)
    )
 
// def this() = this(")
// scala.append((for(col <- cols) yield scalaInitArgFor(col)).mkString(", "))
// scala.append((for( col <- cols ) yield scalaForColumnDefinition(col)).mkString(",\n"))
    
   s.toList.map( _.toScala ).mkString("")
    
  }


  def generate(connection: Connection, srcRoot: String, pkg: String, schema: String) = {

    println("tables")

    // TODO

    for( t <- tables.where({ t => t.TABLE_SCHEMA === schema }) ) {
      println("TABLE " + t.TABLE_NAME)
      print(scalaForTable(t, pkg))
      println()
    }
    
  }

  

}
