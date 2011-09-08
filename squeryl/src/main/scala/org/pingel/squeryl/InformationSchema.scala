package org.pingel.squeryl

import org.pingel.squeryl.information_schema.entities._
import org.squeryl._

object InformationSchema extends Schema {

  val tables = table[Tables]("tables")
  
  val columns = table[Columns]("columns")
  
}
