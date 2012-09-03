package axle

case class EnrichedByteArray(barr: Array[Byte]) {
  def ⊕(other: Array[Byte]): Array[Byte] = barr.zip(other).map(lr => (lr._1 ^ lr._2).toByte).toArray
}
