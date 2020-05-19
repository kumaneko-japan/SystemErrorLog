/*
 * 致命的なエラー、Spring MVC管理外で発生する例外を、ログに出力するためのFilterクラス。
 * ログは、すべてのERRORレベルで出力する。
 * このクラスを利用した場合、致命的なエラー、およびSpring MVC管理外で発生するすべての例外を、
 * ログに出力することができる。
 * ログは、ExceptionLoggerを使用して出力している。
 */
package syserrLog.webException;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.filter.GenericFilterBean;

import lombok.Getter;
import lombok.Setter;
import syserrLog.commonException.ExceptionLogger;

@Getter
@Setter
public class ExceptionFilter extends GenericFilterBean {

	//処理されない例外のログを出力するクラス
	/*
	 * ①『ServletException』
	 * サーブレットで問題が発生したときにスローできる一般的な例外を定義
	 *
	 * ②『IOException』
	 * なんらかの入出力例外の発生を通知するシグナルを発生させる
	 * 入出力処理の失敗、または割り込みの発生によって生成される例外の汎用クラス
	 *
	 * ③『RuntimeException』
	 * Java仮想マシンの通常の処理でスローすることができる各種の例外のスーパー・クラス
	 */

	//例外出力用のLoggerオブジェクト
    private ExceptionLogger exceptionLogger = null;

    //フィルターを実行し、例外が発生した場合、例外をログに記録
    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (IOException e) {
            logIOException(e, servletRequest, servletResponse);
            throw e;
        } catch (ServletException e) {
            logServletException(e, servletRequest, servletResponse);
            throw e;
        } catch (RuntimeException e) {
            logRuntimeException(e, servletRequest, servletResponse);
            throw e;
        }
    }

    //例外フィルターを初期化する
    @Override
    protected void initFilterBean() throws ServletException {
        if (exceptionLogger == null) {
            exceptionLogger = new ExceptionLogger(getClass().getName());
            exceptionLogger.afterPropertiesSet();
        }
    }

    //IOExceptionのエラーログを出力
    protected void logIOException(IOException ex, ServletRequest request,ServletResponse response) {
        exceptionLogger.error(ex);
    }

    //ServletExceptionのエラーログを出力
    protected void logServletException(ServletException ex,ServletRequest request, ServletResponse response) {
        exceptionLogger.error(ex);
    }

    //RuntimeExceptionのエラーログを出力
    protected void logRuntimeException(RuntimeException ex,ServletRequest request, ServletResponse response) {
        exceptionLogger.error(ex);
    }

}
