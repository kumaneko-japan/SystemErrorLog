/*
 * 例外クラスに対応する例外レベル（ログレベル）を解決するためのインタフェース。
 * 例外レベルとは、どのようなレベルの例外が発生したのかを識別するためのコードで、
 * ログの出力レベルを切り替えるために使われる。
 */
package syserrLog.commonException;

public interface ExceptionLevelResolver {
    ExceptionLevel resolveExceptionLevel(Exception ex);
}
