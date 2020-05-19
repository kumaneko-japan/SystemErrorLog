/*
 * 例外をログ出力するためのクラス。
 * 監視ログとアプリケーションログを出力することができる。
 */
package syserrLog.commonException;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionLogger implements InitializingBean {

    private static final String MONITORING_LOG_LOGGER_SUFFIX = ".Monitoring";

    private final Logger applicationLogger;

    private final Logger monitoringLogger;

    private final Map<ExceptionLevel, LogLevelWrappingLogger> exceptionLevelLoggers = new ConcurrentHashMap<ExceptionLevel, LogLevelWrappingLogger>();

    private final InfoLog infoLog;

    private final WarnLog warnLog;

    private final ErrorLog errorLog;

    private ExceptionCodeResolver exceptionCodeResolver = new MapCodeResolver();

    private ExceptionLevelResolver exceptionLevelResolver;

    private String PLACEHOLDER_OF_EXCEPTION_CODE = "{0}";

    private String PLACEHOLDER_OF_EXCEPTION_MESSAGE = "{1}";

    private String logMessageFormat = String.format("[%s] %s",PLACEHOLDER_OF_EXCEPTION_CODE, PLACEHOLDER_OF_EXCEPTION_MESSAGE);

    private String defaultCode = "UNDEFINED-CODE";

    private String defaultMessage = "UNDEFINED-MESSAGE";

    private boolean trimLogMessage = true;

    public ExceptionLogger() {
        this(ExceptionLogger.class.getName());
    }

    public ExceptionLogger(String name) {
        this.applicationLogger = LoggerFactory.getLogger(name);
        this.monitoringLogger = LoggerFactory.getLogger(name
                + MONITORING_LOG_LOGGER_SUFFIX);
        this.infoLog = new InfoLog();
        this.warnLog = new WarnLog();
        this.errorLog = new ErrorLog();
    }

    @Override
    public void afterPropertiesSet() {

    	validateFormat(logMessageFormat);

        if (exceptionLevelResolver == null) {
            exceptionLevelResolver = new FirstCharacterResolver(exceptionCodeResolver);
        }
        registerExceptionLevelLoggers(ExceptionLevel.INFO, infoLog);
        registerExceptionLevelLoggers(ExceptionLevel.WARN, warnLog);
        registerExceptionLevelLoggers(ExceptionLevel.ERROR, errorLog);
    }

    public void log(Exception ex) {
        ExceptionLevel level = exceptionLevelResolver.resolveExceptionLevel(ex);
        LogLevelWrappingLogger logger = null;
        if (level != null) {
            logger = exceptionLevelLoggers.get(level);
        }
        if (logger == null) {
            logger = errorLog;
        }
        log(ex, logger);
    }

    public void info(Exception ex) {
        log(ex, infoLog);
    }

    public void warn(Exception ex) {
        log(ex, warnLog);
    }

    public void error(Exception ex) {
        log(ex, errorLog);
    }

    protected void validateFormat(String logMessageFormat) {
        if (logMessageFormat == null || !logMessageFormat.contains(
                PLACEHOLDER_OF_EXCEPTION_CODE) || !logMessageFormat.contains(
                        PLACEHOLDER_OF_EXCEPTION_MESSAGE)) {
            String message = "logMessageFormat must have placeholder({0} and {1})."
                    + " {0} is replaced with exception code."
                    + " {1} is replaced with exception message. current logMessageFormat is \""
                    + logMessageFormat + "\".";
            throw new IllegalArgumentException(message);
        }
    }

    protected String resolveExceptionCode(Exception ex) {
        String exceptionCode = null;
        if (exceptionCodeResolver != null) {
            exceptionCode = exceptionCodeResolver.resolveExceptionCode(ex);
        }
        return exceptionCode;
    }

    protected String makeLogMsg(Exception ex) {
        String exceptionCode = resolveExceptionCode(ex);
        return formatMsg(exceptionCode, ex.getMessage());
    }

    protected String formatMsg(String exceptionCode,
            String exceptionMessage) {

        String bindingExceptionCode = exceptionCode;
        String bindingExceptionMessage = exceptionMessage;
        if (StringUtils.isEmpty(bindingExceptionCode)) {
            bindingExceptionCode = defaultCode;
        }
        if (StringUtils.isEmpty(bindingExceptionMessage)) {
            bindingExceptionMessage = defaultMessage;
        }

        String message = MessageFormat.format(logMessageFormat,
                bindingExceptionCode, bindingExceptionMessage);
        if (trimLogMessage) {
            message = message.trim();
        }
        return message;
    }

    protected void registerExceptionLevelLoggers(ExceptionLevel level,LogLevelWrappingLogger logger) {
        this.exceptionLevelLoggers.put(level, logger);
    }

    private void log(Exception ex, LogLevelWrappingLogger logger) {
        if (!logger.isEnabled()) {
            return;
        }
        String logMessage = makeLogMsg(ex);
        logger.log(logMessage, ex);
    }

    protected interface LogLevelWrappingLogger {
        boolean isEnabled();
        void log(String logMessage, Exception ex);
    }

    private final class InfoLog implements LogLevelWrappingLogger {
        @Override
        public boolean isEnabled() {
            return monitoringLogger.isInfoEnabled() || applicationLogger.isInfoEnabled();
        }

        @Override
        public void log(String logMessage, Exception ex) {
            if (monitoringLogger.isInfoEnabled()) {
                monitoringLogger.info(logMessage);
            }
            if (applicationLogger.isInfoEnabled()) {
                applicationLogger.info(logMessage, ex);
            }
        }
    }

    private final class WarnLog implements LogLevelWrappingLogger {

        @Override
        public boolean isEnabled() {
            return monitoringLogger.isWarnEnabled() || applicationLogger.isWarnEnabled();
        }

        @Override
        public void log(String logMessage, Exception ex) {
            if (monitoringLogger.isWarnEnabled()) {
                monitoringLogger.warn(logMessage);
            }
            if (applicationLogger.isWarnEnabled()) {
                applicationLogger.warn(logMessage, ex);
            }
        }
    }

    private final class ErrorLog implements LogLevelWrappingLogger {
        @Override
        public boolean isEnabled() {
            return monitoringLogger.isErrorEnabled() || applicationLogger.isErrorEnabled();
        }

        @Override
        public void log(String logMessage, Exception ex) {
            if (monitoringLogger.isErrorEnabled()) {
                monitoringLogger.error(logMessage);
            }
            if (applicationLogger.isErrorEnabled()) {
                applicationLogger.error(logMessage, ex);
            }
        }
    }

}
