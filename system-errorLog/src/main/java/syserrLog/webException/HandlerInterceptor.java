/*
 * HandlerExceptionResolverでハンドリングされた例外を、ログ出力するためのInterceptorクラス。
 * このクラスでは、HandlerExceptionResolverで解決されたHttpレスポンスコードの分類に応じて、
 * ログの出力レベルを切り替えている。
 * 1.100-399 の場合は、INFOレベルで出力する。
 * 2.400-499 の場合は、WARNレベルで出力する。
 * 3.500- の場合は、ERRORレベルで出力する。
 * 4.-99 の場合は、ログ出力しない。
 * このクラスを使用することで、Spring MVC管理下で発生するすべての例外をログに出力することができる。
 * ログは、ExceptionLoggerを使用して出力している。
 */
package syserrLog.webException;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.Getter;
import lombok.Setter;
import syserrLog.commonException.ExceptionLogger;
import syserrLog.commonException.NoticeException;

@Getter
@Setter
public class HandlerInterceptor implements MethodInterceptor,InitializingBean {

	//アプリケーションログを出力するためのLogger
    private static final Logger logger = LoggerFactory.getLogger(
    		HandlerInterceptor.class);

    //例外出力用のLoggerオブジェクト
    private ExceptionLogger exceptionLogger = null;

    //ログ出力を行わない例外クラス
    private Set<Class<? extends Exception>> ignoreExceptions;

    //コンストラクタ
    public HandlerInterceptor() {
        this.ignoreExceptions = new HashSet<Class<? extends Exception>>();
        ignoreExceptions.add(NoticeException.class);
    }

    //HTTPステータスコードが100〜399の場合はINFOログ、400〜499の場合はWARNログ、500以上の場合はエラーログが出力
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object returnObj = invocation.proceed();
        if (returnObj == null) {
            return returnObj;
        }

        Object targetObject = invocation.getThis();
        if (!(targetObject instanceof HandlerExceptionResolver)) {
            if (logger.isWarnEnabled()) {
                logger.warn(
                	"target object does not implement the HandlerExceptionResolver interface. target object is '{}'.",
                    targetObject.getClass().getName());
            }
            return returnObj;
        }

        Exception exception = (Exception) invocation.getArguments()[3];
        if (isTargetException(exception)) {
            HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[0];
            HttpServletResponse response = (HttpServletResponse) invocation.getArguments()[1];
            Object handler = invocation.getArguments()[2];
            log(exception, request, response, handler);
        }

        return returnObj;
    }

    //HandlerExceptionResolverLoggingInterceptorを初期化
    @Override
    public void afterPropertiesSet() throws Exception {
        if (exceptionLogger == null) {
            exceptionLogger = new ExceptionLogger(getClass().getName());
            exceptionLogger.afterPropertiesSet();
        }
    }

    //ログが出力されるクラスのリストに例外クラスが含まれているかどうかを判別
    protected boolean isTargetException(Exception ex) {
        if (ignoreExceptions == null) {
            return true;
        }
        for (Class<? extends Exception> ignoreClass : ignoreExceptions) {
            if (ignoreClass.isInstance(ex)) {
                return false;
            }
        }
        return true;
    }

    //ログを出力
    protected void log(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {

    	//Httpステータスコードを取得
        int statusCode = response.getStatus();
        if (HttpServletResponse.SC_INTERNAL_SERVER_ERROR <= statusCode) {
            // Httpステースタスコード 500～
            logServerError(ex, request, response, handler);
            return;
        }
        if (HttpServletResponse.SC_BAD_REQUEST <= statusCode) {
            // Httpステータスコード 400～499
            logClientError(ex, request, response, handler);
            return;
        }
        if (HttpServletResponse.SC_MULTIPLE_CHOICES <= statusCode) {
            // Httpステータスコード 300～399
            logRedirection(ex, request, response, handler);
            return;
        }
        if (HttpServletResponse.SC_OK <= statusCode) {
            // Httpステータスコード 200～299
            logSuccess(ex, request, response, handler);
            return;
        }
        if (HttpServletResponse.SC_CONTINUE <= statusCode) {
            // Httpステータスコード 100～199
            logInformational(ex, request, response, handler);
            return;
        }

    }

    //ステータスコードが 100～199 の場合ログ出力
    protected void logInformational(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.info(ex);
    }

    //ステータスコードが 200～299 の場合ログ出力
    protected void logSuccess(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.info(ex);
    }

    //ステータスコードが 300～399 の場合ログ出力
    protected void logRedirection(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.info(ex);
    }

    //ステータスコードが 400～499 の場合ログ出力
    protected void logClientError(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.warn(ex);
    }

    //ステータスコードが 500～ の場合ログ出力
    protected void logServerError(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.error(ex);
    }

}
