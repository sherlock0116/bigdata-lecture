package cgs.bigdata.util.log

import org.slf4j._

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
trait Logging {
	
	@transient  private var _log: Logger = _
	
	protected def logName: String = {
		this.getClass.getName.stripSuffix("$")
	}
	
	protected def log: Logger = {
		if (_log == null) {
			_log = LoggerFactory.getLogger(logName)
		}
		_log
	}
	
	// Log methods that take only a String
	protected def logInfo(msg: => String): Unit = {
		if (log.isInfoEnabled) log.info(msg)
	}
	
	protected def logDebug(msg: => String): Unit = {
		if (log.isDebugEnabled) log.debug(msg)
	}
	
	protected def logTrace(msg: => String): Unit = {
		if (log.isTraceEnabled) log.trace(msg)
	}
	
	protected def logWarning(msg: => String): Unit = {
		if (log.isWarnEnabled) log.warn(msg)
	}
	
	protected def logError(msg: => String): Unit = {
		if (log.isErrorEnabled) log.error(msg)
	}
	
	// Log methods that take Throwables (Exceptions/Errors) too
	protected def logInfo(msg: => String, throwable: Throwable): Unit = {
		if (log.isInfoEnabled) log.info(msg, throwable)
	}
	
	protected def logDebug(msg: => String, throwable: Throwable): Unit = {
		if (log.isDebugEnabled) log.debug(msg, throwable)
	}
	
	protected def logTrace(msg: => String, throwable: Throwable): Unit = {
		if (log.isTraceEnabled) log.trace(msg, throwable)
	}
	
	protected def logWarning(msg: => String, throwable: Throwable): Unit = {
		if (log.isWarnEnabled) log.warn(msg, throwable)
	}
	
	protected def logError(msg: => String, throwable: Throwable): Unit = {
		if (log.isErrorEnabled) log.error(msg, throwable)
	}
	
	protected def isTraceEnabled(): Boolean = {
		log.isTraceEnabled
	}
}
