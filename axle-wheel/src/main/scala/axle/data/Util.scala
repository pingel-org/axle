package axle.data

import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

trait Util {

  val dataCacheDir = "data_cache/"

  def urlToCachedFile(source: URL, filename: String): File = {

    val file = new File(dataCacheDir + filename)

    if (!file.exists) {
      Files.copy(source.openStream(), Paths.get(dataCacheDir + filename), StandardCopyOption.REPLACE_EXISTING)
    }

    file
  }

}
