
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

  protected def warn(msg: => AnyRef, t: => Throwable = null): Unit = {
    if (logger.isWarnEnabled) {
      if (t != null) {
        logger.warn(msg.toString, t)
      } else {
        logger.warn(msg.toString)
      }
    }
  }

  protected def error(msg: => AnyRef, t: => Throwable = null): Unit = {
    if (logger.isErrorEnabled) {
      if (t != null) {
        logger.error(msg.toString, t)
      } else {
        logger.error(msg.toString)
      }
    }
  }

}
