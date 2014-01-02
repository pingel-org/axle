package axle.hbase

import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.util.Bytes

object Implicits {

  implicit def string2array(s: String): Array[Byte] = Bytes.toBytes(s)
  // Bytes.toString

  implicit def enrichHTable(table: HTable): EnrichedHTable = new EnrichedHTable(table)

}
