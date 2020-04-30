package axle

import java.io._ 
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

import collection.JavaConverters._

import cats.effect._ 
import cats.effect.concurrent.Semaphore
import cats.implicits._ 

object IO {

  def classpathResourceAsString(filename: String): String = {

    // TODO wrap this in cats.effect
    val stream = this.getClass.getResourceAsStream(filename)
    val result = scala.io.Source.fromInputStream(stream).mkString
    stream.close()
    result
  }

  /**
   * 
   * Returns the actual amount of bytes transmitted
   * 
   * From https://typelevel.org/cats-effect/tutorial/tutorial.html
   */

  def transmit[F[_]: Sync](
    origin: InputStream, destination: OutputStream, buffer: Array[Byte], acc: Long): F[Long] =
    for {
      amount <- Sync[F].delay(origin.read(buffer, 0, buffer.size))
      count  <- if(amount > -1) Sync[F].delay(destination.write(buffer, 0, amount)) >> transmit(origin, destination, buffer, acc + amount)
                else Sync[F].pure(acc) // End of read stream reached (by java.io.InputStream contract), nothing to write
    } yield count // Returns the actual amount of bytes transmitted

  def transfer[F[_]: Sync](origin: InputStream, destination: OutputStream): F[Long] =
    for {
      buffer <- Sync[F].delay(new Array[Byte](1024 * 10)) // Allocated only when the IO is evaluated
      total  <- transmit(origin, destination, buffer, 0L)
    } yield total

  def inputStream[F[_]: Sync](f: File, guard: Semaphore[F]): Resource[F, FileInputStream] =
    Resource.make {
      Sync[F].delay(new FileInputStream(f))
    } { inStream => 
      guard.withPermit {
       Sync[F].delay(inStream.close()).handleErrorWith(_ => Sync[F].unit)
      }
    }
  
  def outputStream[F[_]: Sync](f: File, guard: Semaphore[F]): Resource[F, FileOutputStream] =
    Resource.make {
      Sync[F].delay(new FileOutputStream(f))
    } { outStream =>
      guard.withPermit {
       Sync[F].delay(outStream.close()).handleErrorWith(_ => Sync[F].unit)
      }
    }

  def readAllLines[F[_]: ContextShift: Sync](bufferedReader: BufferedReader, blocker: Blocker): F[List[String]] =
    blocker.delay[F, List[String]] {
      bufferedReader.lines().iterator().asScala.toList
    }

  def readContent[F[_]: ContextShift: Sync](bufferedReader: BufferedReader, blocker: Blocker): F[String] =
    blocker.delay[F, String] {
      bufferedReader.lines().iterator().asScala.mkString
    }

  def reader[F[_]: ContextShift: Sync](file: File, blocker: Blocker): Resource[F, BufferedReader] =
    Resource.fromAutoCloseableBlocking(blocker)(Sync[F].delay {
      new BufferedReader(new FileReader(file))
    })

  def readContentFromFile[F[_]: ContextShift: Sync](file: File, blocker: Blocker): F[String] = {
    reader(file, blocker).use(br => readContent(br, blocker))
  }

  def readLinesFromFile[F[_]: ContextShift: Sync](file: File, blocker: Blocker): F[List[String]] = {
    reader(file, blocker).use(br => readAllLines(br, blocker))
  }

  def convertStreamToString(is: InputStream): String = {
    val result = scala.io.Source.fromInputStream(is, "UTF-8").getLines().mkString("\n")
    is.close
    result
  }

  // TODO wrap this in cats.effect
  def findLeaves(dirname: String, suffix: String): List[String] =
    _findLeaves(new File(dirname), suffix)

  // TODO wrap this in cats.effect
  def _findLeaves(dir: File, suffix: String): List[String] =
    dir.listFiles.toList
      .flatMap({ file =>
        if (file.isDirectory) {
          _findLeaves(file, suffix).map(h => file.getName + File.separator + h)
        } else {
          if (file.getName.endsWith(suffix)) {
            List(file.getName)
          } else {
            Nil
          }
        }
      })

  def urlToCachedFileToLines[F[_]: ContextShift: Sync](
    source: URL,
    cacheDir: String,
    filename: String,
    blocker: Blocker): F[List[String]] = {

    val file = new File(cacheDir + filename)

    if (!file.exists) {
      Files.copy(source.openStream(), Paths.get(cacheDir + filename), StandardCopyOption.REPLACE_EXISTING)
    }

    readLinesFromFile(file, blocker)
  }
}