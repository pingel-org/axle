package axle.data

import java.io._ 
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import collection.JavaConverters._
import cats.effect._ 
import cats.effect.concurrent.Semaphore
import cats.implicits._ 

object Util {

  /**
   * 
   * Returns the actual amount of bytes transmitted
   * 
   * From https://typelevel.org/cats-effect/tutorial/tutorial.html
   */

  def transmit(
    origin: InputStream,
    destination: OutputStream,
    buffer: Array[Byte],
    acc: Long): IO[Long] =
    for {
      amount <- IO(origin.read(buffer, 0, buffer.size))
      count  <- if(amount > -1) IO(destination.write(buffer, 0, amount)) >> transmit(origin, destination, buffer, acc + amount)
                else IO.pure(acc) // End of read stream reached (by java.io.InputStream contract), nothing to write
    } yield count

  def transfer(origin: InputStream, destination: OutputStream): IO[Long] =
    for {
      buffer <- IO(new Array[Byte](1024 * 10)) // Allocated only when the IO is evaluated
      total  <- transmit(origin, destination, buffer, 0L)
    } yield total

  def inputStream(f: File, guard: Semaphore[IO]): Resource[IO, FileInputStream] =
    Resource.make {
      IO(new FileInputStream(f))
    } { inStream => 
      guard.withPermit {
       IO(inStream.close()).handleErrorWith(_ => IO.unit)
      }
    }
  
  def outputStream(f: File, guard: Semaphore[IO]): Resource[IO, FileOutputStream] =
    Resource.make {
      IO(new FileOutputStream(f))
    } { outStream =>
      guard.withPermit {
       IO(outStream.close()).handleErrorWith(_ => IO.unit)
      }
    }

  def readAllLines[F[_]: ContextShift: Sync](bufferedReader: BufferedReader, blocker: Blocker): F[List[String]] =
    blocker.delay[F, List[String]] {
      bufferedReader.lines().iterator().asScala.toList
    }

  def reader[F[_]: ContextShift: Sync](file: File, blocker: Blocker): Resource[F, BufferedReader] =
    Resource.fromAutoCloseableBlocking(blocker)(Sync[F].delay {
      new BufferedReader(new FileReader(file))
    })

  def readLinesFromFile[F[_]: ContextShift: Sync](file: File, blocker: Blocker): F[List[String]] = {
    reader(file, blocker).use(br => readAllLines(br, blocker))
  }

  val dataCacheDir = "data_cache/"

  def urlToCachedFileToLines[F[_]: ContextShift: Sync](
    source: URL,
    filename: String,
    blocker: Blocker): F[List[String]] = {

    val file = new File(dataCacheDir + filename)

    if (!file.exists) {
      Files.copy(source.openStream(), Paths.get(dataCacheDir + filename), StandardCopyOption.REPLACE_EXISTING)
    }

    readLinesFromFile(file, blocker)
  }

}
