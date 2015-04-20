package axle

import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.util.Bytes
import scala.language.implicitConversions

package object hbase {

  implicit def string2array(s: String): Array[Byte] = Bytes.toBytes(s)
  // Bytes.toString

  implicit def enrichHTable(table: HTable): EnrichedHTable = new EnrichedHTable(table)

}