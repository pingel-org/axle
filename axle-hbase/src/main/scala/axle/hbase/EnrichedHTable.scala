package axle.hbase

import collection.JavaConverters._
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.Delete

class EnrichedHTable(table: HTable) {

  import Implicits._

  def size: Long = 0L

  def put(row: Array[Byte], family: Array[Byte], qual: Array[Byte], value: Array[Byte]): Unit = {
    val theput = new Put(row)
    theput.add(family, qual, value)
    table.put(theput)
  }

  def get(row: Array[Byte]): Array[Byte] =
    table.get(new Get(row)).value()

  def delete(row: Array[Byte]): Unit =
    table.delete(new Delete(row))

  // TODO return Iterator (but remember that scanner.close must wait until it's been traversed)
  def scan(family: Array[Byte], qual: Array[Byte]): Vector[(Array[Byte], Long, Array[Byte])] = {
    val sc = new Scan()
    sc.addColumn(family, qual)
    val scanner = table.getScanner(sc)
    try {
      scanner.iterator.asScala.map(rr => {
        val row = rr.getRow()
        val kv = rr.getColumnLatest(family, qual)
        val timestamp = kv.getTimestamp()
        val value = kv.getValue()
        (row, timestamp, value)
      }).toVector
    } finally {
      scanner.close()
    }
  }

  def close(): Unit = table.close()

}
