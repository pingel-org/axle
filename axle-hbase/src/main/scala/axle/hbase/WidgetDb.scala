
package axle.hbase

import Implicits._
import scala.concurrent.duration.FiniteDuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.client.HTable
import Implicits.enrichHTable
import org.apache.hadoop.hbase.util.Bytes

case class Widget(name: String, description: String)

class WidgetDb {

  import HBaseConfig._
  import Implicits._

  val family = "cf"
  val qualifier = "a"

  val widgetTable = table("widget")

  def size: Long = widgetTable.size

  def insert(widget: Widget): Unit = {
    val key = widget.name
    val value = widget.description
    widgetTable.put(key, family, qualifier, value)
  }

  def queryWidgets(name: String): Map[Long, Widget] =
    widgetTable
      .scan(family, qualifier)
      .map({ case (row, t, value) => (t, Widget(name, Bytes.toString(value))) })
      .toMap

  def shutdown(): Unit = widgetTable.close()

}
