
package org.pingel.squeryl.information_schema.entities

import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations.Column
import org.squeryl.adapters.MySQLAdapter

case class Columns(
    var TABLE_CATALOG: String, // varchar(512)
    var TABLE_SCHEMA: String,  // varchar(64)
    var TABLE_NAME: String,    // varchar(64)
    var COLUMN_NAME: String,   // varchar(64)
    var ORDINAL_POSITION: Long, // bigint(21) unsigned
    var COLUMN_DEFAULT: Option[String]=None, // longtext
    var IS_NULLABLE: String,   // varchar(3)
    var DATA_TYPE: String,     // varchar(64)
    var CHARACTER_MAXIMUM_LENGTH: Option[Long], // bigint(21) unsigned
    var CHARACTER_OCTET_LENGTH: Option[Long],   // bigint(21) unsigned
    var NUMERIC_PRECISION: Option[Long],        // bigint(21) unsigned
    var NUMERIC_SCALE: Option[Long], // bigint(21) unsigned
    var CHARACTER_SET_NAME: Option[String], // varchar(32)
    var COLLATION_NAME: Option[String],     // varchar(32)
    var COLUMN_TYPE: String,   // longtext
    var COLUMN_KEY: String,    // varchar(3)
    var EXTRA: String,         // varchar(27)
    var PRIVILEGES: String,    // varchar(80)
    var COLUMN_COMMENT: String // varchar(1024)
)
{

  def this() = this("","","","",0L,
		    Some(""),"","",Some(0L),Some(0L),
		    Some(0L),Some(0L),Some(""),Some(""),
		    "","","","","")

}
