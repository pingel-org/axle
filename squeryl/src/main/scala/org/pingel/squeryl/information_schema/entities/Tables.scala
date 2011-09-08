
package org.pingel.squeryl.information_schema.entities

import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations.Column
import org.squeryl.adapters.MySQLAdapter

import java.sql.Timestamp

case class Tables(
  var TABLE_CATALOG: String,  // varchar(512)
  var TABLE_SCHEMA: String,   // varchar(64)
  var TABLE_NAME: String,     // varchar(64)
  var TABLE_TYPE: String,     // varchar(64)
  var ENGINE: Option[String], // varchar(64)
  var VERSION: Option[Long],  // bigint(21) unsigned
  var ROW_FORMAT: Option[String], // varchar(10)        
  var TABLE_ROWS: Option[Long],   // bigint(21) unsigned
  var AVG_ROW_LENGTH: Option[Long], // bigint(21) unsigned
  var DATA_LENGTH: Option[Long],  // bigint(21) unsigned
  var MAX_DATA_LENGTH: Option[Long], // bigint(21) unsigned
  var INDEX_LENGTH: Option[Long], // bigint(21) unsigned
  var DATA_FREE: Option[Long],       // bigint(21) unsigned
  var AUTO_INCREMENT: Option[Long],  // bigint(21) unsigned
  var CREATE_TIME: Option[Timestamp], // datetime
  var UPDATE_TIME: Option[Timestamp], // datetime
  var CHECK_TIME: Option[Timestamp],  // datetime
  var TABLE_COLLATION: Option[String],  // varchar(32)        
  var CHECKSUM: Option[Long],  // bigint(21) unsigned
  var CREATE_OPTIONS: Option[String],  // varchar(255)       
  var TABLE_COMMENT: String // varchar(2048)
)
{
  
  def this() = this("","","","",Some(""),
		    Some(0L),Some(""),Some(0L),Some(0L),Some(0L),
		    Some(0L),Some(0L),Some(0L),Some(0L),None,
		    None,None,Some(""),Some(0L),Some(""),
		    ""
		  )
  
}
