/*
 * 例外クラスに対応する例外コード（メッセージID）を解決するためのインターフェイス
 * 例外コードとは、どのような例外が発生したのかを識別するためのコードで、
 * システムエラー画面や、ログに出力することを想定している。
 */
package syserrLog.commonException;

public interface ExceptionCodeResolver {
    String resolveExceptionCode(Exception ex);

}
