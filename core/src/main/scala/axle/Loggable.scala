
package axle

import org.slf4j.LoggerFactory

trait Loggable {

  private lazy val logger = LoggerFactory.getLogger(getClass)

  protected def debug(msg: => AnyRef): Unit = {
    if (logger.isDebugEnabled) {
      logger.debug(msg.toString)
    }
  }

  protected def info(msg: => AnyRef): Unit = {
    if (logger.isInfoEnabled) {
      logger.info(msg.toString)
    }
  }

  protected def warn(msg: => AnyRef, t: => Option[Throwable] = None): Unit = {
    if (logger.isWarnEnabled) {
      t.map(logger.warn(msg.toString, _)).getOrElse(logger.warn(msg.toString))
    }
  }

  protected def error(msg: => AnyRef, t: => Option[Throwable] = None): Unit = {
    if (logger.isErrorEnabled) {
      t.map(logger.error(msg.toString, _)).getOrElse(logger.error(msg.toString))
    }
  }

}
