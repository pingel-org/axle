
package org.pingel.squeryl

import org.squeryl.Session
import org.squeryl.SessionFactory
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.adapters.MySQLAdapter
import org.squeryl.annotations.Column
import java.sql.DriverManager

object CLI {

  val src = "src/main/scala"

  def main(args: Array[String]) = {

    val jdbcDsn = "jdbc:mysql://localhost/information_schema?user=root&password="
      
    Class.forName("com.mysql.jdbc.Driver")
    val connection = DriverManager.getConnection(jdbcDsn)

    SessionFactory.concreteFactory = Some(() => Session.create(connection,
							       new MySQLAdapter()))

    SessionFactory.newSession.bindToCurrentThread 

    val generator = new MysqlToSqueryl()

    generator.generate(connection, src,
		       "org.pingel.squeryl.information_schema.entities",
		       "information_schema")

    generator.generate(connection, src,
		       "org.pingel.squeryl.test.entities", "test")

  }

}
