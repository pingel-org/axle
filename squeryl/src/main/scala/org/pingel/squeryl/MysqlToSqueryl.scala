
package org.pingel.squeryl

import java.sql.Connection

import scala.collection._

import org.pingel.squeryl.InformationSchema._
import org.pingel.squeryl.information_schema.entities._
import org.squeryl.PrimitiveTypeMode._

class MysqlToSqueryl() {

  // def getX = x.lookup(id)

  def getTableBySchemaAndName(schema: String, name: String) =
    tables.where( t => t.TABLE_NAME === name and t.TABLE_SCHEMA === schema ).single

  def getColumnsByTable(t: Tables) = 
    columns.where( c => c.TABLE_NAME === t.TABLE_NAME and c.TABLE_SCHEMA === t.TABLE_SCHEMA ) // TODO: orderBy(c.ORDINAL_POSITION asc)


  def scalaTypeFor(c: Columns) = "String" // TODO

  def scalaInitArgFor(c: Columns) = "None" // TODO

  def scalaForColumnDefinition(c: Columns) = 
    "  var " + c.COLUMN_NAME + ": " + scalaTypeFor(c)

  def scalaForTable(t: Tables, pkg: String) = {

    val scala = new mutable.ListBuffer[String]()

    val cols = getColumnsByTable(t)

    scala.appendAll(List(
      "package "+pkg,
      "",
      "import org.squeryl._",
      "import org.squeryl.PrimitiveTypeMode._",
      "import org.squeryl.annotations.Column",
      "import org.squeryl.adapters.MySQLAdapter",
      "",
      "import java.sql._", // TODO compute only what's needed
      "",
      "case class " + t.TABLE_NAME.toUpperCase + "("
      ))

    scala.append((
      for( col <- cols ) yield scalaForColumnDefinition(col)).mkString(",\n"))

    scala.appendAll(List(
      ")",
      "{",
      "",
      "  def this() = this("))

    scala.append((
      for(col <- cols) yield scalaInitArgFor(col)).mkString(", "))

    scala.appendAll(List(
      "  )",
      "",
      "}"))

    scala.mkString("\n")
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
