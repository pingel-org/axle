
package axle.hbase

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.HTableDescriptor

object HBaseConfig {

  val conf = HBaseConfiguration.create()
  conf.set("hbase.zookeeper.quorum", "localhost")
  conf.set("hbase.zookeeper.property.clientPort", "2181")

  val admin = new HBaseAdmin(conf)

  def tables(): List[HTableDescriptor] = admin.listTables.toList

  def table(name: String): EnrichedHTable = new HTable(conf, name)

}
