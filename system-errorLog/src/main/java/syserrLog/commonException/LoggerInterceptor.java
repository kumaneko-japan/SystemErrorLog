/*
 * ResultMessagesを保持している例外が発生したことをログに出力するためのInterceptorクラス。
 * ログはすべてWARNレベルで出力する。
 * @Serviceアノテーションが付与されているクラスのメソッドに対して適用することを想定。
 * ログは、ExceptionLoggerを使用して出力している。
 */
package syserrLog.commonException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

import lombok.Getter;

@Getter
public class LoggerInterceptor implements MethodInterceptor, InitializingBean {

    private final ThreadLocal<MethodInvocation> startingPoint = new ThreadLocal<MethodInvocation>();

    private ExceptionLogger exceptionLogger = null;

    public void setExceptionLogger(ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }

    @Override
    public Object invoke(
            MethodInvocation invocation) throws Throwable, NoticeException {

        if (startingPoint.get() == null) {
            startingPoint.set(invocation);
        }

        try {
            return invocation.proceed();
        } catch (NoticeException e) {
            if (isStartingPoint(invocation)) {
                logNoticeException(e);
            }
            throw e;
        } finally {
            if (isStartingPoint(invocation)) {
                startingPoint.remove();
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (exceptionLogger == null) {
            exceptionLogger = new ExceptionLogger(getClass().getName());
            exceptionLogger.afterPropertiesSet();
        }
    }

    protected boolean isStartingPoint(MethodInvocation invocation) {
        return startingPoint.get() == invocation;
    }

    protected void logNoticeException(
    		NoticeException e) {
        exceptionLogger.warn(e);
    }

}